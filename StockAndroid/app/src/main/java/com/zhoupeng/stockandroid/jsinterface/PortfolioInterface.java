package com.zhoupeng.stockandroid.jsinterface;

import android.webkit.JavascriptInterface;

import com.zhoupeng.stockandroid.bean.Stockhold;

public class PortfolioInterface {

    private String ticker;

    private Integer quantity;
    private Double avgCost;
    private Double price;
    private Double totalCost;
    private Double change;
    private Double marketValue;

    public PortfolioInterface(Stockhold stockhold) {
        this.ticker = stockhold.getTicker();
        this.quantity = stockhold.getQuantity();
        this.avgCost = stockhold.getAvgCost();
        this.price = stockhold.getPrice();
        this.totalCost = stockhold.getTotalCost();
        this.change = stockhold.getChange();
        this.marketValue = stockhold.getMarketValue();
    }

    public void setPortfolioInterface(Stockhold stockhold) {
        this.ticker = stockhold.getTicker();
        this.quantity = stockhold.getQuantity();
        this.avgCost = stockhold.getAvgCost();
        this.price = stockhold.getPrice();
        this.totalCost = stockhold.getTotalCost();
        this.change = stockhold.getChange();
        this.marketValue = stockhold.getMarketValue();
    }

    @JavascriptInterface
    public String getTicker() {
        return ticker;
    }

    @JavascriptInterface
    public String getQuantity() {
        return quantity.toString();
    }

    @JavascriptInterface
    public String getAvgCost() {
        return String.format("%.2f", avgCost);
    }

    @JavascriptInterface
    public String getPrice() {
        return String.format("%.2f", price);
    }

    @JavascriptInterface
    public String getTotalCost() {
        return String.format("%.2f", totalCost);
    }

    @JavascriptInterface
    public String getChange() {
        return String.format("%.2f", change);
    }

    @JavascriptInterface
    public String getMarketValue() {
        return String.format("%.2f", marketValue);
    }
}
