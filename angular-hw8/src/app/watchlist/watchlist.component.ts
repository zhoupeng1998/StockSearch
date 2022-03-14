import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContextService } from '../context.service';
import { DataService } from '../data.service';
import { WatchlistService } from '../watchlist.service';

interface WatchObject {
  ticker: string,
  symbol: string,
  latest: Object
}

@Component({
  selector: 'app-watchlist',
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css']
})
export class WatchlistComponent implements OnInit {

  watchlistEmptyAlert: boolean;
  watchlistObject: {[key: string]: Object};
  watchlist: any[] = [];

  constructor(
    private router: Router,
    private context: ContextService,
    private dataService: DataService,
    private watchlistService: WatchlistService
  ) {
    this.watchlistEmptyAlert = true;
    this.watchlistObject = watchlistService.getWatchlistObject();
    for (var k in this.watchlistObject) {
      var item = Object(this.watchlistObject[k]);
      if (Number(item.latest.dp) < 0) {
        item.color = 'text-danger';
      } else {
        item.color = 'text-success';
      }
      this.watchlist.push(item);
      dataService.getLatestDataBySymbol(item.symbol);
    }
    this.watchlistEmptyAlert = this.watchlist.length == 0;
  }

  // TODO: complete on card click feature
  onCardClick(symbol: string) {
    this.context.setSearchSymbol(symbol);
    this.context.setCardSwitchFlag(true);
    this.context.setNavbarCardSwitchFlag(true);
    this.router.navigateByUrl('/search/'+symbol);
  }

  onRemoveButtonClick(ticker: string) {
    this.watchlistService.removeFromWatchlist(ticker);
    this.watchlist.forEach((item, index) => {
      if (item.ticker == ticker) {
        this.watchlist.splice(index, 1);
      }
    });
    this.watchlistEmptyAlert = this.watchlist.length == 0;
  }

  ngOnInit(): void {
    this.dataService.latestDataBySymbolReadySubject.subscribe(data => {
      if (data == null || data == '') {
        return;
      }
      var dataobj = JSON.parse(data);
      for (var i = 0; i < this.watchlist.length; i++) {
        if (dataobj.symbol == this.watchlist[i].symbol) {
          this.watchlist[i].latest.c = dataobj.c;
          this.watchlist[i].latest.d = dataobj.d;
          this.watchlist[i].latest.dp = dataobj.dp;
          if (Number(dataobj.dp) < 0) {
            this.watchlist[i].color = "text-danger";
          } else {
            this.watchlist[i].color = "text-success";
          }
        }
        // TODO: update data to localstorage
      }
    })
  }

}
