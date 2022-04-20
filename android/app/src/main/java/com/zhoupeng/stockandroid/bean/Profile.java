package com.zhoupeng.stockandroid.bean;

public class Profile {
    private String ticker;
    private String name;
    private String exchange;
    private String ipo;
    private String logo;
    private String weburl;
    private String industry;

    public Profile() {
    }

    public Profile(String ticker, String name, String exchange, String ipo, String logo, String weburl) {
        this.ticker = ticker;
        this.name = name;
        this.exchange = exchange;
        this.ipo = ipo;
        this.logo = logo;
        this.weburl = weburl;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getIpo() {
        return ipo;
    }

    public void setIpo(String ipo) {
        this.ipo = ipo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getIndustry() {
        return industry;
    }

    public void setFinnhubIndustry(String industry) {
        this.industry = industry;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "ticker='" + ticker + '\'' +
                ", name='" + name + '\'' +
                ", exchange='" + exchange + '\'' +
                ", ipo='" + ipo + '\'' +
                ", logo='" + logo + '\'' +
                ", weburl='" + weburl + '\'' +
                ", industry='" + industry + '\'' +
                '}';
    }
}