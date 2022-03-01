const express = require('express');
const axios = require('axios');
require('dotenv').config()

const app = express();
const token = process.env.API_KEY;

const candle_router = require('./routes/candle');
require('./utils/dateformat');
const filternews = require('./utils/newsfilter');

app.listen(5000);

app.get("/", (req, res) => {
    var ts = new Date(Date.now());
    ts.setDate(ts.getDate() - 2);
    ts.setHours(ts.getHours() - 6);
    var last = new Date();
    last.setHours(ts.getHours() - 6);
    res.send({str: String(ts), num: Number(ts), last: Number(last)});
});

// Profile
app.get("/api/profile/:symbol", (req, res) => {
    var symbol = req.params.symbol.trim().toUpperCase();
    axios.get('https://finnhub.io/api/v1/stock/profile2', {
        params: {
            token: token,
            symbol: symbol
        }
    }).then(data => {
        res.send(data.data);
    }).catch(err => {
        res.send({error: err});
    });
});

// Latest Data
app.get("/api/latest/:symbol", (req, res) => {
    var symbol = req.params.symbol.trim().toUpperCase();
    axios.get('https://finnhub.io/api/v1/quote', {
        params: {
            token: token,
            symbol: symbol
        }
    }).then(data => {
        res.send(data.data);
    }).catch(err => {
        res.send({error: err});
    });
});

// Autocomplete API
app.get("/api/autocomplete/:symbol", (req, res) => {
    var symbol = req.params.symbol.trim().toUpperCase();
    axios.get('https://finnhub.io/api/v1/search', {
        params: {
            token: token,
            q: symbol
        }
    }).then(data => {
        let result = data.data.result;
        result = result.filter(feed => {
            if (feed.type == null || feed.symbol == null || feed.symbol.indexOf('.') !== -1) {
                return false;
            }
            return feed.type == "Common Stock";
        });
        result.forEach(element => {
            delete element.type;
            delete element.primary;
        });
        res.send(result);
    }).catch(err => {
        res.send({error: err});
    })
});

// News
app.get("/api/news/:symbol", (req, res) => {
    var symbol = req.params.symbol.trim().toUpperCase();
    var endtime = new Date(Date.now());
    var begintime = new Date(endtime);
    begintime.setDate(begintime.getDate() - 7);
    axios.get('https://finnhub.io/api/v1/company-news', {
        params: {
            token: token,
            symbol: symbol,
            from: begintime.format("yyyy-MM-dd"),
            to: endtime.format("yyyy-MM-dd")
        }
    }).then(data => {
        res.send(filternews(data.data));
    }).catch(err => {
        res.send({error: err});
    });
});

// Recommendation
app.get("/api/recommendation/:symbol", (req, res) => {
    var symbol = req.params.symbol.trim().toUpperCase();
    axios.get('https://finnhub.io/api/v1/stock/recommendation', {
        params: {
            token: token,
            symbol: symbol
        }
    }).then(data => {
        res.send(data.data);
    }).catch(err => {
        res.send({error: err});
    });
});

// Social Sentiment
// TODO: question, clarification
app.get("/api/social/:symbol", (req, res) => {
    var symbol = req.params.symbol.trim().toUpperCase();
    axios.get('https://finnhub.io/api/v1/stock/social-sentiment', {
        params: {
            token: token,
            symbol: symbol
        }
    }).then(data => {
        res.send(data.data);
    }).catch(err => {
        res.send({error: err});
    });
});

// Peers
app.get("/api/peers/:symbol", (req, res) => {
    var symbol = req.params.symbol.trim().toUpperCase();
    axios.get('https://finnhub.io/api/v1/stock/peers', {
        params: {
            token: token,
            symbol: symbol
        }
    }).then(data => {
        res.send(data.data);
    }).catch(err => {
        res.send({error: err});
    });
});

// Earnings
app.get("/api/earnings/:symbol", (req, res) => {
    var symbol = req.params.symbol.trim().toUpperCase();
    axios.get('https://finnhub.io/api/v1/stock/earnings', {
        params: {
            token: token,
            symbol: symbol
        }
    }).then(data => {
        let result = data.data;
        result.forEach(element => {
            if (element.actual == null) {
                element.actual = 0
            }
            if (element.estimate == null) {
                element.estimate = 0
            }
            if (element.surprise == null) {
                element.surprise = 0
            }
            if (element.surprisePercent == null) {
                element.surprisePercent = 0 
            }
        });
        res.send(data.data);
    }).catch(err => {
        res.send({error: err});
    });
});


app.use(candle_router);