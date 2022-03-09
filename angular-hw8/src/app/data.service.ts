import { HttpClient } from '@angular/common/http';
import { Injectable, Inject } from '@angular/core';
import { ContextService } from './context.service';
import axios from 'axios';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  profileDataReadySubject: Subject<boolean> = new Subject();
  latestDataReadySubject: Subject<boolean> = new Subject();
  newsDataReadySubject: Subject<boolean> = new Subject();
  recommendationDataReadySubject: Subject<boolean> = new Subject();
  socialSentimentDataReadySubject: Subject<boolean> = new Subject();
  peersDataReadySubject: Subject<boolean> = new Subject();
  earningsDataReadySubject: Subject<boolean> = new Subject();
  summaryChartReadySubject: Subject<boolean> = new Subject();
  historyChartReadySubject: Subject<boolean> = new Subject();

  constructor(
    private http: HttpClient,
    private context: ContextService) { }

  getAllData() {
    this.getProfileData();
    this.getLatestData(); // calls getSummaryChartsData
    this.getNewsData();
    this.getRecommendationData();
    this.getSocialSentimentData();
    this.getPeersData();
    this.getEarningsData();
    this.getHistoryChartData();
  }

  getProfileData() {
    var url = "http://localhost/api/profile/" + this.context.getSearchSymbol();
    axios.get(url).then(data => {
      var valid = data.data.ticker != null;
      window.localStorage.setItem('profile', JSON.stringify(data.data));
      this.profileDataReadySubject.next(valid);
    }).catch(() => {
      window.localStorage.setItem('profile', "");
      this.profileDataReadySubject.next(false);
    });
  }

  getLatestData() {
    var tnow = Date.now();
    var tnowNum = new Date(tnow);
    var url = "http://localhost/api/latest/" + this.context.getSearchSymbol();
    axios.get(url).then(data => {
      if (data.data.t != null && data.data.t != 0) {
        var tret = parseInt(data.data.t) * 1000;
        data.data.diff = tnow - tret;
        if (tnow - tret < 60000) {
          // handle market open (parseInt(tnowNum / 1000))
          data.data.marketopen = true;
          this.getSummaryChartData(Math.trunc(Number(tnowNum) / 1000));
        } else {
          // handle market closed (data.data.t)
          data.data.marketopen = false;
          this.getSummaryChartData(data.data.t);
        }
        window.localStorage.setItem('latest', JSON.stringify(data.data));
        this.latestDataReadySubject.next(true);
      }
    }).catch(() => {
      window.localStorage.setItem('latest', "");
      this.latestDataReadySubject.next(false);
    });
  }

  getNewsData() {
    var url = "http://localhost/api/news/" + this.context.getSearchSymbol();
    axios.get(url).then(data => {
      window.localStorage.setItem('news', JSON.stringify(data.data));
      this.newsDataReadySubject.next(true);
    }).catch(() => {
      window.localStorage.setItem('news', "");
      this.newsDataReadySubject.next(false);
    });
  }

  getRecommendationData() {
    var url = "http://localhost/api/recommendation/" + this.context.getSearchSymbol();
    axios.get(url).then(data => {
      window.localStorage.setItem('recommendation', JSON.stringify(data.data));
      this.recommendationDataReadySubject.next(true);
    }).catch(() => {
      window.localStorage.setItem('recommendation', "");
      this.recommendationDataReadySubject.next(false);
    });
  }

  getSocialSentimentData() {
    var url = "http://localhost/api/social/" + this.context.getSearchSymbol();
    axios.get(url).then(data => {
      window.localStorage.setItem('social', JSON.stringify(data.data));
      this.socialSentimentDataReadySubject.next(true);
    }).catch(() => {
      window.localStorage.setItem('social', "");
      this.socialSentimentDataReadySubject.next(false);
    });
  }

  getPeersData() {
    var url = "http://localhost/api/peers/" + this.context.getSearchSymbol();
    axios.get(url).then(data => {
      window.localStorage.setItem('peers', JSON.stringify(data.data));
      this.peersDataReadySubject.next(true);
    }).catch(() => {
      window.localStorage.setItem('peers', "");
      this.peersDataReadySubject.next(false);
    });
  }

  getEarningsData() {
    var url = "http://localhost/api/earnings/" + this.context.getSearchSymbol();
    axios.get(url).then(data => {
      window.localStorage.setItem('earnings', JSON.stringify(data.data));
      this.earningsDataReadySubject.next(true);
    }).catch(() => {
      window.localStorage.setItem('earnings', "");
      this.earningsDataReadySubject.next(false);
    });
  }

  getSummaryChartData(timestmp: Number) {
    var url = "http://localhost/api/candle/hour/" + this.context.getSearchSymbol() + "/" + timestmp;
    axios.get(url).then(data => {
      window.localStorage.setItem('summaryChart', JSON.stringify(data.data));
      this.summaryChartReadySubject.next(true);
    }).catch(() => {
      window.localStorage.setItem('summaryChart', "");
      this.summaryChartReadySubject.next(false);
    });
  }

  getHistoryChartData() {
    var url = "http://localhost/api/candle/year/" + this.context.getSearchSymbol();
    axios.get(url).then(data => {
      window.localStorage.setItem('historyChart', JSON.stringify(data.data));
      this.historyChartReadySubject.next(true);
    }).catch(() => {
      window.localStorage.setItem('historyChart', "");
      this.historyChartReadySubject.next(false);
    });
  }
}
