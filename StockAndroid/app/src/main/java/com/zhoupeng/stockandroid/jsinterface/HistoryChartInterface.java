package com.zhoupeng.stockandroid.jsinterface;

import android.webkit.JavascriptInterface;

public class HistoryChartInterface {
    private String ticker;
    private String historyCandleDataString;

    public HistoryChartInterface(String ticker, String historyCandleDataString) {
        this.ticker = ticker;
        this.historyCandleDataString = historyCandleDataString;
    }

    @JavascriptInterface
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    @JavascriptInterface
    public String getHistoryCandleDataString() {
        return historyCandleDataString;
    }

    public void setHistoryCandleDataString(String historyCandleDataString) {
        this.historyCandleDataString = historyCandleDataString;
    }
}
