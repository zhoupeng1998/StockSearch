const express = require("express");
const axios = require('axios');
require('dotenv').config();

const router = express.Router();
const token = process.env.API_KEY;

// Get 6 hours of historical data with resolution of 5
// For summary tab chart only
// example-short: test 1645693500
router.get("/api/candle/hour/:symbol/:time", (req, res) => {
    var symbol = req.params.symbol.trim().toUpperCase();
    var endtime = new Date(parseInt(req.params.time) * 1000);
    var begintime = new Date(endtime);
    begintime.setHours(begintime.getHours() - 6);
    axios.get('https://finnhub.io/api/v1/stock/candle', {
        params: {
            token: token,
            symbol: symbol,
            resolution: 5,
            from: Number(begintime) / 1000,
            to: Number(endtime) / 1000
        }
    }).then(data => {
        res.send(data.data);
    }).catch(err => {
        res.send({error: err});
    });
})

// Get 2 years of historical data with resolution of D
// For Charts tab
router.get("/api/candle/year/:symbol", (req, res) => {
    var symbol = req.params.symbol.trim().toUpperCase();
    var endtime = new Date(Date.now());
    var begintime = new Date(endtime);
    begintime.setFullYear(begintime.getFullYear() - 2);
    axios.get('https://finnhub.io/api/v1/stock/candle', {
        params: {
            token: token,
            symbol: symbol,
            resolution: "D",
            from: parseInt(Number(begintime) / 1000),
            to: parseInt(Number(endtime) / 1000)
        }
    }).then(data => {
        res.send(data.data);
    }).catch(err => {
        res.send({error: err});
    });
});

module.exports = router;