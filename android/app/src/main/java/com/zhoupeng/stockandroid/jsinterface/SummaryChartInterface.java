package com.zhoupeng.stockandroid.jsinterface;

import android.webkit.JavascriptInterface;

public class SummaryChartInterface {
    private String ticker;
    private String summaryCandleDataString;
    private String dp;

    public SummaryChartInterface(String ticker, String summaryCandleDataString, String dp) {
        this.ticker = ticker;
        this.summaryCandleDataString = summaryCandleDataString;
        this.dp = dp;
    }

    @JavascriptInterface
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    @JavascriptInterface
    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    @JavascriptInterface
    public String getSummaryCandleDataString() {
        return summaryCandleDataString;
    }

    public void setSummaryCandleDataString(String summaryCandleDataString) {
        this.summaryCandleDataString = summaryCandleDataString;
    }
}
