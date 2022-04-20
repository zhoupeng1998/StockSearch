package com.zhoupeng.stockandroid.bean;

public class FavoriteItem {

    private String ticker;
    private String companyName;
    private Double price;
    private Double delta;
    private Double dp;

    public FavoriteItem() {
    }

    public FavoriteItem(String ticker, String companyName, Double price, Double delta, Double dp) {
        this.ticker = ticker;
        this.companyName = companyName;
        this.price = price;
        this.delta = delta;
        this.dp = dp;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDelta() {
        return delta;
    }

    public void setDelta(Double delta) {
        this.delta = delta;
    }

    public Double getDp() {
        return dp;
    }

    public void setDp(Double dp) {
        this.dp = dp;
    }
}
