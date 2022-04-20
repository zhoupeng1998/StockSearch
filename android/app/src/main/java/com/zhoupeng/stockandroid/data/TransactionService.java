package com.zhoupeng.stockandroid.data;

import com.zhoupeng.stockandroid.bean.Stockhold;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class TransactionService extends Observable {

    private Double balance;
    private Stockhold recentAddedStockhold;
    private Map<String, Stockhold> stockholds;

    private static class TransactionServiceSingletonInstance {
        private static final TransactionService instance = new TransactionService();
    }

    private TransactionService() {
        this.balance = 25000.0;
        this.stockholds = new HashMap<>();
    }

    public static TransactionService getInstance() {
        return TransactionServiceSingletonInstance.instance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

    public Double getNetWorth() {
        Double result = this.balance;
        for (Map.Entry<String, Stockhold> entry : this.stockholds.entrySet()) {
            result += entry.getValue().getMarketValue();
        }
        return result;
    }

    public Map<String, Stockhold> getStockholds() {
        return stockholds;
    }

    public Stockhold getStockholdByTicker(String ticker) {
        Stockhold stockhold = stockholds.get(ticker);
        if (stockhold == null) {
            stockhold = new Stockhold(ticker);
            stockholds.put(ticker, stockhold);
        }
        return stockhold;
    }

    public Stockhold buyStockByTicker(String ticker, Integer quantity, Double unitPrice) {
        Stockhold stockhold = getStockholdByTicker(ticker);
        if (stockhold == null) {
            stockhold = new Stockhold(ticker);
            recentAddedStockhold = stockhold;
        } else if (stockhold.getQuantity() == 0) {
            recentAddedStockhold = stockhold;
        }
        stockhold.buyStock(quantity, unitPrice);
        stockholds.put(ticker, stockhold);
        this.balance -= quantity * unitPrice;
        setChanged();
        notifyObservers();
        // TODO: to local storage / database
        return stockhold;
    }

    public Stockhold sellStockByTicker(String ticker, Integer quantity, Double unitPrice) {
        Stockhold stockhold = getStockholdByTicker(ticker);
        stockhold.sellStock(quantity, unitPrice);
        if (stockhold.getQuantity() <= 0) {
            stockholds.remove(ticker);
        } else {
            stockholds.put(ticker, stockhold);
        }
        this.balance += quantity * unitPrice;
        setChanged();
        notifyObservers();
        // TODO: to local storage / database
        recentAddedStockhold = null;
        return stockhold;
    }

    public Stockhold getRecentAddedStockhold() {
        Stockhold item = recentAddedStockhold;
        recentAddedStockhold = null;
        return item;
    }

    public void addEntry(String key, Stockhold stockhold) {
        this.stockholds.put(key, stockhold);
    }
}
