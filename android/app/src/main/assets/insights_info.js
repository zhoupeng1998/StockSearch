$('companyName').html(InsightsInterface.getCompanyName());
$('#redditTotal').html(InsightsInterface.getRedditMention());
$('#twitterTotal').html(InsightsInterface.getTwitterMension());
$('#redditPositive').html(InsightsInterface.getRedditPositiveMention());
$('#twitterPositive').html(InsightsInterface.getTwitterPositiveMention());
$('#redditNegative').html(InsightsInterface.getRedditNegativeMention());
$('#twitterNegative').html(InsightsInterface.getTwitterNegativeMention());

var recommendationPeriods = JSON.parse(InsightsInterface.getRecommendationPeriods());
var recommendationStrongBuy = JSON.parse(InsightsInterface.getRecommendationStrongBuy());
var recommendationBuy = JSON.parse(InsightsInterface.getRecommendationBuy());
var recommendationHold = JSON.parse(InsightsInterface.getRecommendationHold());
var recommendationSell = JSON.parse(InsightsInterface.getRecommendationSell());
var recommendationStrongSell = JSON.parse(InsightsInterface.getRecommendationStrongSell());

var earningsPeriods = JSON.parse(InsightsInterface.getEarningsPeriods());
var earningsActual = JSON.parse(InsightsInterface.getEarningsActual());
var earningsEstimate = JSON.parse(InsightsInterface.getEarningsEstimate());

Highcharts.chart('recommendationContainer', {
    title: {
        text: "Recommendation Trends"
    },
    chart: {
        type: 'column'
    },
    xAxis: {
        categories: recommendationPeriods
    },
    yAxis: {
        min: 0,
        title: {
            text: '#Analysis'
        },
        stackLabels: {
            enabled: false,
            style: {
                fontWeight: 'bold'
            }
        }
    },
    plotOptions: {
        column: {
            stacking: 'normal',
            dataLabels: {
                enabled: true
            }
        }
    },
    series: [
        {
            type: 'column',
            name: 'Strong Buy',
            data: recommendationBuy,
            color: '#18632f'
        },
        {
            type: 'column',
            name: 'Buy',
            data: recommendationBuy,
            color: '#19b049'
        },
        {
            type: 'column',
            name: 'Hold',
            data: recommendationHold,
            color: '#af7f1b'
        },
        {
            type: 'column',
            name: 'Sell',
            data: recommendationSell,
            color: '#f15050'
        },
        {
            type: 'column',
            name: 'Strong Sell',
            data: recommendationStrongSell,
            color: '#742c2e'
        },
    ]
});

Highcharts.chart('earningsContainer', this.earningsChartOptions = {
    title: {
        text: "Historical EPS Surprises"
    },
    xAxis: {
        categories: earningsPeriods
    },
    yAxis: {
        title: {
            text: 'Quantity EPS'
        },
    },
    tooltip: {
        shared: true,
    },
    series: [
        {
            data: earningsActual,
            name: 'Actual',
            type: 'spline'
        },
        {
            data: earningsEstimate,
            name: 'Estimate',
            type: 'spline'
        }
    ]
});