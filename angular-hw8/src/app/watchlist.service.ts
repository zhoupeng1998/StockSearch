import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class WatchlistService {

  constructor() { }

  getWatchlistObject() {
    var watchlist: {[key: string]: Object} = JSON.parse(window.localStorage.getItem('watchlist') || "{}");
    return watchlist;
  }

  isTickerInWatchlist(ticker: string) {
    var watchlist: {[key: string]: Object} = JSON.parse(window.localStorage.getItem('watchlist') || "{}");
    return watchlist[ticker] != null;
  }

  addToWatchlist(symbol: string, ticker: string, name: string, latest: Object) {
    var watchlist: {[key: string]: Object} = JSON.parse(window.localStorage.getItem('watchlist') || "{}");
    watchlist[ticker] = {ticker: ticker, symbol: symbol, name: name, latest: latest};
    window.localStorage.setItem('watchlist', JSON.stringify(watchlist));
    this.isTickerInWatchlist(ticker);
  }
  
  removeFromWatchlist(ticker: string) {
    var watchlist: {[key: string]: Object} = JSON.parse(window.localStorage.getItem('watchlist') || "{}");
    delete watchlist[ticker];
    window.localStorage.setItem('watchlist', JSON.stringify(watchlist));
  }

  // TODO: complete function
  /*
  updateWatchlistItemByTicker(ticker: string, latest: Object) {
    var watchlist: {[key: string]: Object} = JSON.parse(window.localStorage.getItem('watchlist') || "{}");
    var obj = watchlist[ticker];
    if (obj == null) {
      return;
    }
    //watchlist[ticker] = {ticker: obj['ticker'], }
  }
  */

}
