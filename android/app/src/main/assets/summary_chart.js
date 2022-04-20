const timezone = new Date().getTimezoneOffset();

var ticker = SummaryChartInterface.getTicker();
var data_raw = SummaryChartInterface.getSummaryCandleDataString();
var dp = SummaryChartInterface.getDp();
var data = JSON.parse(data_raw);

var color = 'green';
if (Number(dp) < 0) {
    color = 'red';
}

Highcharts.setOptions({
    global: {
        timezoneOffset: timezone
    }
});

for (var i = 0; i < data.t.length; i++) {
    data.t[i] = data.t[i] * 1000;
}

Highcharts.chart('container', {
      title: {
        text: `${ticker} Hourly Price Variation`
      },
      xAxis: {
        type: 'datetime',
        labels: {
          enabled: true,
          format: '{value:%H:%M}',
        },
        categories: data.t,
        tickInterval: 10
      },
      yAxis: {
        title: {
          text: ''
        },
        opposite: true
      },
      series: [{
        showInLegend: false,
        data: data.c,
        type: 'line',
        name: 'Price',
        marker: {
          radius: 0,
          lineWidth: 1,
        },
        color: color
      }]
    });