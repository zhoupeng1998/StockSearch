package com.zhoupeng.stockandroid.bean;

public class Stockhold {
    private String ticker;
    private Integer quantity;
    private Double totalCost;
    private Double avgCost;
    private Double price;
    private Double marketValue;

    public Stockhold() {
    }

    public Stockhold(String ticker) {
        this.ticker = ticker;
        this.quantity = 0;
        this.totalCost = 0.0;
        this.avgCost = 0.0;
        this.price = 0.0;
        this.marketValue = 0.0;
    }

    public String getTicker() {
        return ticker;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public Double getAvgCost() {
        return avgCost;
    }

    public void setPrice(Double price) {
        this.price = price;
        this.marketValue = price * this.quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Double getMarketValue() {
        return marketValue;
    }

    public Double getChange() {
        if (quantity == 0) {
            return 0.0;
        }
        return this.price - this.avgCost;
    }

    public void buyStock(Integer quantity, Double unitPrice) {
        this.price = unitPrice;
        this.totalCost += quantity * unitPrice;
        this.quantity += quantity;
        this.avgCost = this.totalCost / this.quantity;
        this.marketValue = this.price * this.quantity;
    }

    public void sellStock(Integer quantity, Double unitPrice) {
        this.price = unitPrice;
        this.totalCost -= quantity * this.avgCost;
        this.quantity -= quantity;
        if (this.quantity == 0) {
            this.avgCost = 0.0;
        } else {
            this.avgCost = this.totalCost / this.quantity;
        }
        this.marketValue = this.price * this.quantity;
    }

    @Override
    public String toString() {
        return "Stockhold{" +
                "ticker='" + ticker + '\'' +
                ", quantity=" + quantity +
                ", totalCost=" + totalCost +
                ", avgCost=" + avgCost +
                ", price=" + price +
                ", marketValue=" + marketValue +
                '}';
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public void setAvgCost(Double avgCost) {
        this.avgCost = avgCost;
    }

    public void setMarketValue(Double marketValue) {
        this.marketValue = marketValue;
    }
}
