currentBox = '#companyBox'

cachedCompany = {}
cachedSummary = {}
cachedCandle = {}
cachedNews = {}
cachedNoSearch = {}

currentCandle = {}

msg = ""
var timestamp = null

function handleClear() {
    $("#barSec").hide()
    $("#infoSec").hide()
    $("#infoSec").html('');
}

function prepareCompanyContent(object) {
    ticker = object['ticker'];
    let text = `
    <div id="companyInfoSec">
        <img id="companyLogo" alt="" src="${object['logo']}">
        <table class="companyTable">
            <tr class="companyTableR" id="companyTableRTop">
                <th class="companyTableH">Company Name</th>
                <td class="companyTableD">${object['name']}</th>
            </tr>
            <tr class="companyTableR">
                <th class="companyTableH">Stock Ticker Symbol</th>
                <td class="companyTableD">${object['ticker']}</th>
            </tr>
            <tr class="companyTableR">
                <th class="companyTableH">Stock Exchange Code</th>
                <td class="companyTableD">${object['exchange']}</th>
            </tr>
            <tr class="companyTableR">
                <th class="companyTableH">Company IPO Date</th>
                <td class="companyTableD">${object['ipo']}</th>
            </tr>
            <tr class="companyTableR">
                <th class="companyTableH">Category</th>
                <td class="companyTableD">${object['finnhubIndustry']}</th>
            </tr>
        </table>
    </div>
    `
    return text
}

function prepareSummaryContent(object) {
    var changeImgSrc = "";
    var changeImgPercentSrc = "";
    var recommendation = {
        'strongSell': "",
        'sell': "",
        'hold': "",
        'buy': "",
        'strongBuy': ""
    };
    if (object['recommendation']['strongSell'] !== undefined) {
        recommendation['strongSell'] = object['recommendation']['strongSell'];
    }
    if (object['recommendation']['sell'] !== undefined) {
        recommendation['sell'] = object['recommendation']['sell'];
    }
    if (object['recommendation']['hold'] !== undefined) {
        recommendation['hold'] = object['recommendation']['hold'];
    }
    if (object['recommendation']['buy'] !== undefined) {
        recommendation['buy'] = object['recommendation']['buy'];
    }
    if (object['recommendation']['strongBuy'] != undefined) {
        recommendation['strongBuy'] = object['recommendation']['strongBuy'];
    }
    if (object['quote']['d'] != null) {
        if (object['quote']['d'] < 0) {
            changeImgSrc = "/static/img/RedArrowDown.png";
        } else if (object['quote']['d'] > 0) {
            changeImgSrc = "/static/img/GreenArrowUp.png";
        }
    }
    if (object['quote']['dp'] != null) {
        if (object['quote']['dp'] < 0) {
            changeImgPercentSrc = "/static/img/RedArrowDown.png";
        } else if (object['quote']['dp'] > 0) {
            changeImgPercentSrc = "/static/img/GreenArrowUp.png";
        }
    }
    let text = `
    <div id="companyInfoSec">
        <table class="companyTable">
            <tr class="companyTableR" id="companyTableRTop">
                <th class="companyTableH">Stock Ticker Symbol</th>
                <td class="companyTableD">${ticker}</th>
            </tr>
            <tr class="companyTableR">
                <th class="companyTableH">Trading Day</th>
                <td class="companyTableD">${object['quote']['t']}</th>
            </tr>
            <tr class="companyTableR">
                <th class="companyTableH">Previous Closing Price</th>
                <td class="companyTableD">${object['quote']['pc']}</th>
            </tr>
            <tr class="companyTableR">
                <th class="companyTableH">Opening Price</th>
                <td class="companyTableD">${object['quote']['o']}</th>
            </tr>
            <tr class="companyTableR">
                <th class="companyTableH">High Price</th>
                <td class="companyTableD">${object['quote']['h']}</th>
            </tr>
            <tr class="companyTableR">
                <th class="companyTableH">Low Price</th>
                <td class="companyTableD">${object['quote']['l']}</th>
            </tr>
            <tr class="companyTableR">
                <th class="companyTableH">Change</th>
                <td class="companyTableD">${object['quote']['d']}
                    <img class="arrowImg" alt="changeImg" src="${changeImgSrc}">
                </th>
            </tr>
            <tr class="companyTableR">
                <th class="companyTableH">Change Percent</th>
                <td class="companyTableD">${object['quote']['dp']}
                    <img class="arrowImg" alt="changeImg" src="${changeImgPercentSrc}">
                </th>
            </tr>
        </table>
        <div class="summaryTrends">
            <div class="summaryTrendText" id="redTrendText">Strong Sell</div>
            <div class="summaryTrendItem" id="bgTrendA">${recommendation['strongSell']}</div><!--
            --><div class="summaryTrendItem" id="bgTrendB">${recommendation['sell']}</div><!--
            --><div class="summaryTrendItem" id="bgTrendC">${recommendation['hold']}</div><!--
            --><div class="summaryTrendItem" id="bgTrendD">${recommendation['buy']}</div><!--
            --><div class="summaryTrendItem" id="bgTrendE">${recommendation['strongBuy']}</div>
            <div class="summaryTrendText" id="greenTrendText">Strong Buy</div>
        </div>
        <div class="summaryUnder">Recommendation Trends</div>
    </div>
    `
    return text
}

function prepareChartContent() {
    //cachedCandle
    Highcharts.stockChart('chartSec', {
        chart: {
            zoomType: 'xy',
            height: 600
        },
        /*
        xAxis: {
            labels: {
              enabled: true
            },
            categories: currentCandle['t'],
            crosshair: true
        },*/
        yAxis: [{ // Primary yAxis
            labels: {
                format: '{value}'
            },
            title: {
                text: 'Stock Price'
            },
            opposite: false
          }, { // Secondary yAxis
            title: {
                text: 'Volume'
            },
            labels: {
                formatter: function() {
                    return this.value / 1000000 + "M";
                }
            },
            opposite: true
        }],
        rangeSelector: {
            allButtonsEnabled: true,
            buttons: [
                {
                    type: 'day',
                    count: 7,
                    text: '7d',
                    dataGrouping: {
                        forced: true,
                        units: [
                            ['day', [1]]
                        ]
                    }
                }, {
                    type: 'day',
                    count: 15,
                    text: '15d',
                    dataGrouping: {
                        forced: true,
                        units: [
                        ['day', [1]]
                        ]
                    }
                }, {
                    type: 'month',
                    count: 1,
                    text: '1m',
                    dataGrouping: {
                        forced: true,
                        units: [
                        ['day', [1]]
                        ]
                    }
                }, {
                    type: 'month',
                    count: 3,
                    text: '3m',
                    dataGrouping: {
                        forced: true,
                        units: [
                        ['day', [1]]
                        ]
                    }
                }, {
                    type: 'month',
                    count: 6,
                    text: '6m',
                    dataGrouping: {
                        forced: true,
                        units: [
                        ['day', [1]]
                        ]
                    }
                }
            ],
            inputEnabled: false,
            selected: 4
        },
        tooltip: {
            shared: true
        },
        title: {
            text: `Stock Price ${ticker} ${currentCandle['today']}`
        },
        subtitle: {
            text: '<a href=\'https://finnhub.io/\' target="_blank">Source: Finnhub</a>',
            style: {
                "text-decoration": "underline",
                "color": "blue"
            }
        },
        _navigator: {
            enabled: false
        },
        legend: {
            layout: 'vertical',
            align: 'left',
            x: 120,
            verticalAlign: 'top',
            y: 100,
            floating: true,
        },
        plotOptions: {
            area: {
                fillColor: {
                    linearGradient: {
                        x1: 0,
                        y1: 0,
                        x2: 0,
                        y2: 1
                    },
                    stops: [
                        [0, Highcharts.getOptions().colors[0]],
                        [1, Highcharts.color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                    ]
                },
                marker: {
                    radius: 2
                },
                lineWidth: 1,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null
            }
        },
        series: [
            {
                type: 'area',
                name: 'Stock Price',
                data: currentCandle['c'],
                marker: {
                    enabled: null, // auto
                    radius: 0,
                    lineWidth: 1,
                    lineColor: '#ffffff'
                },
                tooltip: {
                    valueDecimals: 2
                }
            }, {
                type: 'column',
                name: 'Volume',
                yAxis: 1,
                maxPointWidth: 10,
                data: currentCandle['v'],
                marker: {
                    enabled: null, // auto
                    color: 'gray',
                    radius: 1,
                    pointWidth: 1,
                    lineWidth: 1,
                    lineColor: '#ffffff'
                }
            }
        ]
    });
}

function prepareNewsContent(object) {
    var text = "<div id=\"newsSec\">\n";
    object.forEach((value, index, array) => {
        text += `
        <div class="newsEntry">
            <div class="newsImgDiv">
                <img class="newsImg" alt="", src="${value['image']}">
            </div>
            <div class="newsContent">
                <p class="newsHead">${value['headline']}</p>
                <p class="newsTime">${value['datetime']}</p>
                <a class="newsLink" href="${value['url']}" target="_blank">See Original Post</a>
            </div>
        </div>
        `;
    })
    text += "</div>"
    return text
}

function prepareChartsData(object) {
    var result = {}
    var price = []
    var volume = []
    for (i = 0; i < object['t'].length; i++) {
        price.push([object['t'][i], object['c'][i]]);
        volume.push([object['t'][i], object['v'][i]]);
    }
    result['c'] = price;
    result['v'] = volume;
    result['today'] = object['today'];
    return result;
}

function prepareErrorContent() {
    return `
    <div id="errorSec">
        Error: No record has been found, please enter a valid symbol
    </div>
    `
}

function handleClear() {
    $("#barSec").hide();
    $("#infoSec").hide();
    $("#infoSec").html('');
}

function handleNotFound() {
    handleClear();
    let text = prepareErrorContent();
    $("#infoSec").html(text);
    $("#infoSec").show();
    currentBox = '#companyBox';
}

function showCompany() {
    let now = Date.now();
    if (msg in cachedNoSearch && now - timestamp < 60000) {
        handleNotFound();
    } else if (msg in cachedCompany && now - timestamp < 60000) {
        $("#infoSec").html(cachedCompany[msg]);
        $("#barSec").show();
        $("#infoSec").show();
    } else {
        $.ajax({
            url: "profile/" + msg,
            type: "GET",
            dataType: "jsonp",
            success: (data) => {
                var result = JSON.stringify(data);
                var object = JSON.parse(result);
                if (object['found'] == 'N') {
                    handleNotFound();
                    cachedNoSearch[msg] = 'N';
                } else {
                    let text = prepareCompanyContent(object);
                    $("#infoSec").html(text);
                    cachedCompany[msg] = text;
                    $("#barSec").show();
                    $("#infoSec").show();
                }
                timestamp = now;
            },
            error: (xhr, type) => {
                handleNotFound();
            }
        });
    }
}

function showSummary() {
    let now = Date.now();
    if (msg in cachedSummary && now - timestamp < 60000) {
        $("#infoSec").html(cachedSummary[msg]);
    } else {
        if (msg in cachedCompany) {
            $.ajax({
                url: "summary/" + msg,
                type: "GET",
                dataType: "jsonp",
                success: (data) => {
                    var result = JSON.stringify(data);
                    var object = JSON.parse(result);
                    let text = prepareSummaryContent(object);
                    $("#infoSec").html(text);
                    cachedSummary[msg] = text;
                    timestamp = now;
                },
                error: (xhr, type) => {
                    handleNotFound();
                }
           });
        } else {
            $.ajax({
                url: "profile/" + msg,
                type: "GET",
                dataType: "jsonp",
                success: (data) => {
                    var result = JSON.stringify(data);
                    var object = JSON.parse(result);
                    if (object['found'] == 'N') {
                        handleNotFound();
                        cachedNoSearch[msg] = 'N';
                    } else {
                        $.ajax({
                            url: "summary/" + msg,
                            type: "GET",
                            dataType: "jsonp",
                            success: (data) => {
                                var result = JSON.stringify(data);
                                var object = JSON.parse(result);
                                let text = prepareSummaryContent(object);
                                $("#infoSec").html(text);
                                cachedSummary[msg] = text;
                                timestamp = now;
                            },
                            error: (xhr, type) => {
                                handleNotFound();
                            }
                       });
                       let text = prepareCompanyContent(object);
                       cachedCompany[msg] = text;
                    }
                    timestamp = now;
                },
                error: (xhr, type) => {
                    handleNotFound();
                }
            });
        }
    }
}

function showCharts() {
    let text = `
        <figure class="figure">
            <div id="chartSec"></div>
        </figure>
    `;
    $("#infoSec").html(text);
    let now = Date.now();
    if (msg in cachedCandle && now - timestamp < 60000) {
        currentCandle = cachedCandle[msg];
        prepareChartContent();
    } else {
        if (msg in cachedCompany) {
            $.ajax({
                url: "candle/" + msg,
                type: "GET",
                dataType: "jsonp",
                success: (data) => {
                    var result = JSON.stringify(data);
                    var object = JSON.parse(result);
                    var processed = prepareChartsData(object);
                    cachedCandle[msg] = processed;
                    currentCandle = processed;
                    console.log(currentCandle)
                    prepareChartContent();
                    timestamp = now;
                },
                error: (xhr, type) => {
                    handleNotFound();
                }
           });
        } else {
            $.ajax({
                url: "profile/" + msg,
                type: "GET",
                dataType: "jsonp",
                success: (data) => {
                    var result = JSON.stringify(data);
                    var object = JSON.parse(result);
                    if (object['found'] == 'N') {
                        handleNotFound();
                        cachedNoSearch[msg] = 'N';
                    } else {
                        $.ajax({
                            url: "candle/" + msg,
                            type: "GET",
                            dataType: "jsonp",
                            success: (data) => {
                                var result = JSON.stringify(data);
                                var object = JSON.parse(result);
                                var processed = prepareChartsData(object);
                                cachedCandle[msg] = processed;
                                currentCandle = processed;
                                console.log(currentCandle)
                                prepareChartContent();
                                timestamp = now;
                            },
                            error: (xhr, type) => {
                                handleNotFound();
                            }
                       });
                       let text = prepareCompanyContent(object);
                       cachedCompany[msg] = text;
                    }
                    timestamp = now;
                },
                error: (xhr, type) => {
                    handleNotFound();
                }
            });
        }
    }
}

function showNews() {
    let now = Date.now();
    if (msg in cachedNews && now - timestamp < 60000) {
        $("#infoSec").html(cachedNews[msg]);
    } else {
        if (msg in cachedCompany) {
            $.ajax({
                url: "news/" + msg,
                type: "GET",
                dataType: "jsonp",
                success: (data) => {
                    var result = JSON.stringify(data);
                    var object = JSON.parse(result);
                    let text = prepareNewsContent(object);
                    $("#infoSec").html(text);
                    cachedNews[msg] = text;
                    timestamp = now;
                },
                error: (xhr, type) => {
                    handleNotFound();
                }
           });
        } else {
            $.ajax({
                url: "profile/" + msg,
                type: "GET",
                dataType: "jsonp",
                success: (data) => {
                    var result = JSON.stringify(data);
                    var object = JSON.parse(result);
                    if (object['found'] == 'N') {
                        handleNotFound();
                        cachedNoSearch[msg] = 'N';
                    } else {
                        $.ajax({
                            url: "news/" + msg,
                            type: "GET",
                            dataType: "jsonp",
                            success: (data) => {
                                var result = JSON.stringify(data);
                                var object = JSON.parse(result);
                                let text = prepareNewsContent(object);
                                $("#infoSec").html(text);
                                cachedNews[msg] = text;
                                timestamp = now;
                            },
                            error: (xhr, type) => {
                                handleNotFound();
                            }
                       });
                       let text = prepareCompanyContent(object);
                       cachedCompany[msg] = text;
                    }
                    timestamp = now;
                },
                error: (xhr, type) => {
                    handleNotFound();
                }
            });
        }
    }
}

function submitInput() {
    var isValid = document.querySelector('#inputForm').reportValidity();
    //currentBox = '#companyBox';
    $(".secBox").css('background-color', '#f8f9fa'); // reset all .secBox color
    //$("#companyBox").css('background-color', 'gray');
    msg = $("#inputField").val().toUpperCase();
    if (msg == '') {
        handleClear();
    } else {
        //currentBox = '#companyBox';
        console.log(currentBox);
        if (currentBox == '#companyBox') {
            $("#companyBox").css('background-color', 'gray');
            showCompany();
        } else if (currentBox == '#summaryBox') {
            $("#summaryBox").css('background-color', 'gray');
            showSummary();
        } else if (currentBox == '#chartsBox') {
            $("#chartsBox").css('background-color', 'gray');
            showCharts();
        } else if (currentBox == '#newsBox') {
            $("#newsBox").css('background-color', 'gray');
            showNews();
        } else {
            $("#companyBox").css('background-color', 'gray');
            showCompany();
        }
    }
}

$(document).ready(() => {

    timestamp = Date.now();

    $("#barSec").hide()

    $("#clearButton").click(() => {
        handleClear();
        $("#inputField").val('');
    });

    $("#searchButton").click(() => {
        submitInput();
    });

    $("#inputForm").submit(e => {
        e.preventDefault();
        submitInput();
    });

    $("#companyBox").hover(() => {
        if (currentBox != '#companyBox') {
            $("#companyBox").css('background-color', 'lightgray')
        }
    }, () => {
        if (currentBox != '#companyBox') {
            $("#companyBox").css('background-color', '#f8f9fa');
        }
    });

    $("#summaryBox").hover(() => {
        if (currentBox != '#summaryBox') {
            $("#summaryBox").css('background-color', 'lightgray')
        }
    }, () => {
        if (currentBox != '#summaryBox') {
            $("#summaryBox").css('background-color', '#f8f9fa');
        }
    });

    $("#chartsBox").hover(() => {
        if (currentBox != '#chartsBox') {
            $("#chartsBox").css('background-color', 'lightgray');
        }
    }, () => {
        if (currentBox != '#chartsBox') {
            $("#chartsBox").css('background-color', '#f8f9fa');
        }
    });

    $("#newsBox").hover(() => {
        if (currentBox != '#newsBox') {
            $("#newsBox").css('background-color', 'lightgray');
        }
    }, () => {
        if (currentBox != '#newsBox') {
            $("#newsBox").css('background-color', '#f8f9fa');
        }
    });

    $(".secBox").click(() => {
        $(".secBox").css('background-color', '#f8f9fa');
    });

    $("#companyBox").click(() => {
        currentBox = '#companyBox'
        $("#companyBox").css('background-color', 'gray');
        showCompany();
    });

    $("#summaryBox").click(() => {
        currentBox = '#summaryBox'
        $("#summaryBox").css('background-color', 'gray');
        showSummary();
    });

    $("#chartsBox").click(() => {
        currentBox = '#chartsBox'
        $("#chartsBox").css('background-color', 'gray');
        showCharts();
    });

    $("#newsBox").click(() => {
        currentBox = '#newsBox'
        $("#newsBox").css('background-color', 'gray');
        showNews();
    });
});