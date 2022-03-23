import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  balance: number;
  stockhold: {[key: string]: any};

  constructor() { 
    var balanceStr = window.localStorage.getItem('balance') || '';
    if (balanceStr == null || balanceStr == '') {
      this.balance = 25000;
    } else {
      this.balance = Number(balanceStr);
    }
    var raw = window.localStorage.getItem('stockhold');
    if (raw == null || raw == '') {
      this.stockhold = {};
    } else {
      this.stockhold = JSON.parse(raw);
    }
  }

  getBalance() {
    return this.balance;
  }

  getStockhold() {
    return this.stockhold;
  }

  isHoldingStock(ticker: string) {
    var stockShare = this.stockhold[ticker];
    if (stockShare == null) {
      return false;
    }
    return stockShare.quantity != 0;
  }

  getHoldingQuantity(ticker: string) {
    if (!this.isHoldingStock(ticker)) {
      return 0;
    }
    var stockShare = this.stockhold[ticker];
    return stockShare.quantity;
  }

  buyStock(ticker: string, symbol: string, name: string, quantity: number, total: number) {
    var stockShare = this.stockhold[ticker];
    if (stockShare == null) {
      stockShare = {quantity: 0, total: 0, avg: 0, symbol: symbol, name: name};
    }
    stockShare.quantity = Number(stockShare.quantity) + Number(quantity);
    stockShare.total = Number(stockShare.total) + Number(total);
    stockShare.avg = Number(stockShare.total) / Number(stockShare.quantity);
    stockShare.symbol = symbol;
    //stockShare.name = name;
    this.stockhold[ticker] = stockShare;
    window.localStorage.setItem('stockhold', JSON.stringify(this.stockhold));
    this.balance -= total;
    this.balance = Math.round(this.balance * 100) / 100;
    window.localStorage.setItem('balance', String(this.balance));
  }

  sellStock(ticker: string, quantity: number, total: number) {
    var stockShare = this.stockhold[ticker];
    if (stockShare == null || stockShare.quantity < quantity) {
      return false;
    }
    stockShare.quantity = Number(stockShare.quantity) - Number(quantity);
    //stockShare.total = Number(stockShare.total) - Number(total);
    stockShare.total = Number(stockShare.total) - (quantity * Number(stockShare.avg));
    console.log("old avg: " + stockShare.avg);
    stockShare.avg = stockShare.total / stockShare.quantity;
    console.log("new avg: " + stockShare.avg);
    this.stockhold[ticker] = stockShare;
    if (stockShare.quantity == 0) {
      delete this.stockhold[ticker];
    }
    window.localStorage.setItem('stockhold', JSON.stringify(this.stockhold));
    this.balance += total;
    this.balance = Math.round(this.balance * 100) / 100;
    window.localStorage.setItem('balance', String(this.balance));
    return true;
  }

}
