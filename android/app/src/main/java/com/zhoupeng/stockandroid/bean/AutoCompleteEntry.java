package com.zhoupeng.stockandroid.bean;

public class AutoCompleteEntry {
    private String description;
    private String displaySymbol;
    private String symbol;

    public AutoCompleteEntry() {
    }

    public AutoCompleteEntry(String description, String displaySymbol, String symbol) {
        this.description = description;
        this.displaySymbol = displaySymbol;
        this.symbol = symbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplaySymbol() {
        return displaySymbol;
    }

    public void setDisplaySymbol(String displaySymbol) {
        this.displaySymbol = displaySymbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return this.displaySymbol + " | " + this.description;
    }
}
