var ticker = HistoryChartInterface.getTicker();
var data_raw = HistoryChartInterface.getHistoryCandleDataString();
var data = JSON.parse(data_raw)

var ohlc = [];
var volume = [];

let groupingUnits = [[
          'week',             // unit name
          [1]               // allowed multiples
        ], [
          'month',
          [1, 2, 3, 4, 6]
        ]]

for (var i = 0; i < data.t.length; i++) {
    ohlc.push([data.t[i]*1000, data.o[i], data.h[i], data.l[i], data.c[i]]);
    volume.push([data.t[i]*1000, data.v[i]]);
}

 Highcharts.stockChart('container', {

    rangeSelector: {
      selected: 2
    },

    title: {
      text: ticker + ' Historical'
    },

    subtitle: {
      text: 'With SMA and Volume by Price technical indicators'
    },

    yAxis: [{
      startOnTick: false,
      endOnTick: false,
      labels: {
        align: 'right',
        x: -3
      },
      title: {
        text: 'OHLC'
      },
      height: '60%',
      lineWidth: 2,
      resize: {
        enabled: true
      }
    }, {
      labels: {
        align: 'right',
        x: -3
      },
      title: {
        text: 'Volume'
      },
      top: '65%',
      height: '35%',
      offset: 0,
      lineWidth: 2
    }],

    tooltip: {
      split: true
    },

    plotOptions: {
      series: {
        dataGrouping: {
          units: groupingUnits
        }
      }
    },

    series: [{
      type: 'candlestick',
      name: ticker,
      id: 'aapl',
      zIndex: 2,
      data: ohlc
    }, {
      type: 'column',
      name: 'Volume',
      id: 'volume',
      data: volume,
      yAxis: 1
    }, {
      type: 'vbp',
      linkedTo: 'aapl',
      params: {
        volumeSeriesID: 'volume'
      },
      dataLabels: {
        enabled: false
      },
      zoneLines: {
        enabled: false
      }
    }, {
      type: 'sma',
      linkedTo: 'aapl',
      zIndex: 1,
      marker: {
        enabled: false
      }
    }]
  });