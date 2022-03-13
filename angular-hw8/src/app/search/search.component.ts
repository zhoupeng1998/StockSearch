import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewInit, ChangeDetectorRef, Renderer2 } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { interval, Subscription, timer } from 'rxjs';
import * as moment from 'moment';
import * as moment_timezone from 'moment-timezone';
import * as Highcharts from 'highcharts';

import { ContextService } from '../context.service';
import { DataService } from '../data.service';
import { NewsModalComponent } from '../news-modal/news-modal.component';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit, OnDestroy, AfterViewInit {
  inputText: String = "";
  noInputFlag: boolean = false;
  validSymbolFlag: boolean = false;
  invalidSymbolFlag: boolean = false;
  loadingFlag: boolean = false;
  clearFlag: boolean = false; // used by onNotify method only!

  // data loading flags
  profileLoadedFlag: boolean = false;
  latestLoadedFlag: boolean = false;
  newsLoadedFlag: boolean = false;
  recommendationLoadedFlag: boolean = false;
  socialSentimentLoadedFlag: boolean = false;
  peersLoadedFlag: boolean = false;
  earningsLoadedFlag: boolean = false;
  summaryChartLoadedFlag: boolean = false;
  historyChartLoadedFlag: boolean = false;

  // data element
  @ViewChild('reservedElement', {static: false}) reservedElement!: ElementRef;
  @ViewChild('displayElement', {static: false}) displayElement!: ElementRef;
  testText1: String = "";
  testText2: String = "";
  testText3: String = "";
  testText4: String = "";
  ticker: String = "";
  name: String = "";
  exchange: String = "";
  ipo: String = "";
  industry: String = "";
  weburl: String = "";
  peers: String = "";
  @ViewChild('logo') logo!: ElementRef;
  c: String = "";
  d: String = "";
  dp: String = "";
  h: String = "";
  l: String = "";
  o: String = "";
  pc: String = "";
  t: String = "";
  @ViewChild('searchPrice') searchPrice!: ElementRef;
  priceUp: boolean = true;
  @ViewChild('marketStatus') marketStatus!: ElementRef;
  formatedQueryTime: String = "";
  @ViewChild('weblink') weblink!: ElementRef;
  @ViewChild('peerlist') peerlist!: ElementRef;
  @ViewChild('newslist') newslist!: ElementRef;
  newscache: any = [];
  redditTotalMentions: string = "";
  redditPositiveMentions: string = "";
  redditNegativeMentions: string = "";
  twitterTotalMentions: string = "";
  twitterPositiveMentions: string = "";
  twitterNegativeMentions: string = "";

  // Highcharts
  highcharts: typeof Highcharts = Highcharts;
  summaryChartOptions: Highcharts.Options = {
    series: [
      {
        data: [],
        type: 'line'
      }
    ]
  };
  recommendationChartOptions: Highcharts.Options = {
    chart: {
      type: 'column'
    },
    plotOptions: {
      column: {
        stacking: 'normal'
      }
    },
    series: [
      {
        type: 'column',
        name: 'Strong Buy',
        data: [],
        color: '#18632f'
      },
      {
        type: 'column',
        name: 'Buy',
        data: [],
        color: '#19b049'
      },
      {
        type: 'column',
        name: 'Hold',
        data: [],
        color: '#af7f1b'
      },
      {
        type: 'column',
        name: 'Sell',
        data: [],
        color: '#f15050'
      },
      {
        type: 'column',
        name: 'Strong Sell',
        data: [],
        color: '#742c2e'
      },
    ]
  }
  earningsChartOptions: Highcharts.Options = {
    title: {
      text: "Historical EPS Surprises"
    },
    tooltip: {
      shared: true,
    },
    series: [
      {
        data: [],
        type: 'spline'
      },
      {
        data: [],
        type: 'spline'
      }
    ]
  };
  

  intervalSubscription: Subscription = new Subscription();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private context: ContextService,
    private dataService: DataService,
    private renderer: Renderer2,
    private modalService: NgbModal,
    private cd: ChangeDetectorRef) { }

  // called only when switch from /watchlist or /portfolio
  ngOnInit(): void {
    /*
    const routeParams = this.route.snapshot.paramMap;
    // TODO: try to distinguish user-input-url search and back-to-home routing here.
    if (routeParams.get('symbol') != null) {
      this.inputText = String(routeParams.get('symbol'));
      if (this.inputText != this.context.getSearchSymbol()) {
        // should reload here!
        this.context.setSearchInput(this.inputText.trim().toUpperCase());
        this.handleSearch();
      }
    } else {
      this.inputText = this.context.getSearchSymbol();
    }
    if (this.context.getValidDataPresentFlag()) {
      // load cached data!
      this.validSymbolFlag = true;
      this.presentAll();
    }
    */

    // initialize timer for periodic update
    const updateTimer = timer(1000, 15000);
    this.intervalSubscription = updateTimer.subscribe(() => {
      if (this.validSymbolFlag) {
        // TODO: do periodic update
        this.dataService.getLatestData();
      }
    });

    // subscribe to setValidDataPresentFlag & services that updates data
    this.context.validDataPresentSubject.subscribe(isValid => {
      this.loadingFlag = false;
      if (this.noInputFlag == true) {
        // do nothing
      } else if (isValid) {
        this.validSymbolFlag = true;
        this.invalidSymbolFlag = false;
        setTimeout(() => { this.presentAll(); });
        //this.presentAll();
      } else {
        this.validSymbolFlag = false;
        this.invalidSymbolFlag = true;
        this.context.setSearchSymbol('home');
      }
    });

    // subscribe for data load completion from DataService, call present functions
    this.dataService.profileDataReadySubject.subscribe(success => {
      if (success) {
        // TODO: set true after all of profile, latest, summary chart are ready
        this.profileLoadedFlag = true;
        //this.context.setValidDataPresentFlag(true);
        //this.presentProfile();
        this.checkDataLoadFlags();
      } else {
        this.context.setValidDataPresentFlag(false);
      }
    });

    this.dataService.latestDataReadySubject.subscribe(() => {
      if (!this.validSymbolFlag) {
        this.latestLoadedFlag = true;
        this.checkDataLoadFlags();
      } else {
        this.presentLatest();
      }
    });

    this.dataService.newsDataReadySubject.subscribe(() => {
      this.newsLoadedFlag = true;
      this.checkDataLoadFlags();
    });

    this.dataService.recommendationDataReadySubject.subscribe(() => {
      this.recommendationLoadedFlag = true;
      this.checkDataLoadFlags();
    });

    this.dataService.socialSentimentDataReadySubject.subscribe(() => {
      this.socialSentimentLoadedFlag = true;
      this.checkDataLoadFlags();
    });

    this.dataService.peersDataReadySubject.subscribe(() => {
      this.peersLoadedFlag = true;
      this.checkDataLoadFlags();
    });

    this.dataService.earningsDataReadySubject.subscribe(() => {
      this.earningsLoadedFlag = true;
      this.checkDataLoadFlags();
    });

    this.dataService.summaryChartReadySubject.subscribe(() => {
      if (!this.validSymbolFlag) {
        this.summaryChartLoadedFlag = true;
        this.checkDataLoadFlags();
      } else {
        this.presentSummaryChart();
      }
    });

    this.dataService.historyChartReadySubject.subscribe(() => {
      this.historyChartLoadedFlag = true;
      this.checkDataLoadFlags();
    });
  }

  ngOnDestroy(): void {
    this.intervalSubscription.unsubscribe();
  }

  ngAfterViewInit(): void {
    const routeParams = this.route.snapshot.paramMap;
    // TODO: try to distinguish user-input-url search and back-to-home routing here.
    if (routeParams.get('symbol') != null) {
      this.inputText = String(routeParams.get('symbol'));
      if (this.inputText != this.context.getSearchSymbol()) {
        // should reload here!
        this.context.setSearchInput(this.inputText.trim().toUpperCase());
        this.handleSearch();
      }
    } else {
      this.inputText = this.context.getSearchSymbol();
    }
    if (this.context.getValidDataPresentFlag()) {
      // load cached data!
      this.validSymbolFlag = true;
      setTimeout(() => { this.presentAll(); });
    }
  }

  onNotify() {
    if (this.context.getClearContentFlag()) {
      this.noInputFlag = false;
      this.validSymbolFlag = false;
      this.invalidSymbolFlag = false;
      this.context.setClearContentFlag(false);
      //this.context.setValidDataPresentFlag(false);
    } else {
      this.handleSearch();
    }
  }

  handleSearch() {
    this.resetDataLoadFlags();
    this.inputText = this.context.getSearchInput();
    if (this.inputText == null || this.inputText.trim().length == 0) {
      this.validSymbolFlag = false;
      this.invalidSymbolFlag = false;
      this.noInputFlag = true;
      this.context.setValidDataPresentFlag(false);
      this.context.setSearchSymbol('home');
    } else {
      this.noInputFlag = false;
      this.router.navigateByUrl('/search/'+this.inputText);
      var realInputText = this.inputText.trim().toUpperCase();
      this.context.setSearchSymbol(realInputText);
      
      // start loading info
      this.loadingFlag = true;
      this.validSymbolFlag = false;
      this.invalidSymbolFlag = false;
      this.dataService.getAllData();
    }
  }

  handlePeerSearch(name: String) {
    this.context.setSearchInput(name);
    this.handleSearch();
  }

  closeNoInputAlert() {
    this.noInputFlag = false;
  }

  // called only when loading cached data
  presentAll() {
    // first page
    this.presentProfile();
    this.presentLatest();
    this.presentSummaryChart();
    // other pages
    this.presentNews();
    this.presentRecommendation();
    this.presentSocialSentiment();
    this.presentPeers();
    this.presentEarnings();
    this.presentHistoryChart();
  }

  // present data fetched from API
  presentProfile() {
    this.testText1 = window.localStorage.getItem('profile') || "";
    var data = JSON.parse(window.localStorage.getItem('profile') || "");
    this.ticker = data.ticker;
    this.name = data.name;
    this.exchange = data.exchange;
    this.logo.nativeElement.src = data.logo;
    if (data.logo == null || data.logo == '') {
      this.logo.nativeElement.src = 'data:image/gif;base64,R0lGODlhAQABAAD/ACwAAAAAAQABAAACADs=';
    }
    this.ipo = data.ipo;
    this.industry = data.finnhubIndustry;
    this.weburl = data.weburl;
    this.weblink.nativeElement.href = data.weburl;
  }

  presentLatest() {
    this.testText2 = window.localStorage.getItem('latest') || "";
    var data = JSON.parse(window.localStorage.getItem('latest') || "");
    this.c = String(data.c);
    this.d = String(data.d);
    this.dp = String(data.dp);
    this.h = String(data.h);
    this.l = String(data.l);
    this.o = String(data.o);
    this.pc = String(data.pc);
    if (data.dp >= 0) {
      this.priceUp = true;
      this.searchPrice.nativeElement.classList.remove("text-danger");
      this.searchPrice.nativeElement.classList.add("text-success");
    } else {
      this.priceUp = false;
      this.searchPrice.nativeElement.classList.remove("text-success");
      this.searchPrice.nativeElement.classList.add("text-danger");
    }
    var latestTime = new Date(data.t * 1000);
    var queryTime = new Date(data.t * 1000 + data.diff);
    var formatedLatestTime = moment(latestTime).format('YYYY-MM-DD HH:mm:ss');
    this.formatedQueryTime = moment(queryTime).format('YYYY-MM-DD HH:mm:ss');
    if (data.marketopen) {
      this.marketStatus.nativeElement.classList.remove("text-danger");
      this.marketStatus.nativeElement.classList.add("text-success");
      this.marketStatus.nativeElement.innerHTML = "Market Open";
    } else {
      this.marketStatus.nativeElement.classList.remove("text-seccess");
      this.marketStatus.nativeElement.classList.add("text-danger");
      this.marketStatus.nativeElement.innerHTML = "Market Closed on " + formatedLatestTime;
    }
  }

  presentNews() {
    var data = JSON.parse(window.localStorage.getItem('news') || "");
    this.testText3 = JSON.stringify(data);
    for (var i = 0; i < data.length; i++) {
      this.newscache[i] = data[i];
      let newsElement = this.renderer.createElement('div');
      //let newsCard = new MatCardModule();
      let newsCard = this.renderer.createElement('mat-card');
      let newsImg = this.renderer.createElement('img');
      let newsText = this.renderer.createElement('div');
      this.renderer.addClass(newsElement, 'col-md');
      this.renderer.addClass(newsElement, 'mt-3');
      this.renderer.addClass(newsCard, 'mat-card');
      this.renderer.addClass(newsCard, 'mat-focus-indicator');
      this.renderer.addClass(newsImg, 'news-img');
      this.renderer.addClass(newsText, 'news-text');
      // NOTE: currently needs to set attribute to all child elements! find a better solution?
      this.renderer.setAttribute(newsCard, 'newsId', String(i));
      this.renderer.setAttribute(newsImg, 'newsId', String(i));
      this.renderer.setAttribute(newsText, 'newsId', String(i));
      this.renderer.setAttribute(newsImg, 'src', data[i].image);
      this.renderer.setProperty(newsText, 'innerHTML', data[i].headline);
      this.renderer.listen(newsCard, 'click', evt => {
        var nid = (<HTMLElement>evt.target!).getAttribute('newsId');
        let newsData = this.newscache[Number(nid)];
        let modalRef = this.modalService.open(NewsModalComponent);
        let date = new Date(newsData.datetime * 1000);
        console.log(date);
        modalRef.componentInstance.source = newsData.source;
        modalRef.componentInstance.date = moment(date).format('MMMM Do, YYYY');
        modalRef.componentInstance.title = newsData.headline;
        modalRef.componentInstance.description = newsData.summary;
        modalRef.componentInstance.url = newsData.url;

      });
      this.renderer.appendChild(newsCard, newsImg);
      this.renderer.appendChild(newsCard, newsText);
      this.renderer.appendChild(newsElement, newsCard);
      this.renderer.appendChild(this.newslist.nativeElement, newsElement);
      if (i % 2 != 0) {
        let rowDivider = this.renderer.createElement('div');
        this.renderer.addClass(rowDivider, 'w-100');
        this.renderer.appendChild(this.newslist.nativeElement, rowDivider);
      }
    }
    // add a dummy div if the number of news feed is odd
    if (data.length % 2 != 0) {
      let dummyElement = this.renderer.createElement('div');
      this.renderer.addClass(dummyElement, 'col-md');
      this.renderer.addClass(dummyElement, 'mt-3');
      this.renderer.appendChild(this.newslist.nativeElement, dummyElement);
    }
  }

  presentRecommendation() {
    var data = JSON.parse(window.localStorage.getItem('recommendation') || "");
    var xCategories = [];
    var xStrongBuy = [];
    var xBuy = [];
    var xHold = [];
    var xSell = [];
    var xStrongSell = [];
    for (var i = 0; i < data.length; i++) {
      xCategories.push(data[i].period);
      xStrongBuy.push(data[i].strongBuy);
      xBuy.push(data[i].buy);
      xHold.push(data[i].hold);
      xSell.push(data[i].sell);
      xStrongSell.push(data[i].strongSell);
    }
    this.recommendationChartOptions = {
      title: {
        text: "Recommendation Trends"
      },
      chart: {
        type: 'column'
      },
      xAxis: {
        categories: xCategories
      },
      yAxis: {
        min: 0,
        title: {
          text: '#Analysis'
        },
        stackLabels: {
          enabled: false,
          style: {
            fontWeight: 'bold'
          }
        }
      },
      plotOptions: {
        column: {
          stacking: 'normal',
          dataLabels: {
            enabled: true
          }
        }
      },
      series: [
        {
          type: 'column',
          name: 'Strong Buy',
          data: xStrongBuy,
          color: '#18632f'
        },
        {
          type: 'column',
          name: 'Buy',
          data: xBuy,
          color: '#19b049'
        },
        {
          type: 'column',
          name: 'Hold',
          data: xHold,
          color: '#af7f1b'
        },
        {
          type: 'column',
          name: 'Sell',
          data: xSell,
          color: '#f15050'
        },
        {
          type: 'column',
          name: 'Strong Sell',
          data: xStrongSell,
          color: '#742c2e'
        },
      ]
    };
    
  }

  presentSocialSentiment() {
    var data = JSON.parse(window.localStorage.getItem('social') || "");
    this.redditTotalMentions = String(data.reddit.mention);
    this.redditPositiveMentions = String(data.reddit.positiveMention);
    this.redditNegativeMentions = String(data.reddit.negativeMention);
    this.twitterTotalMentions = String(data.twitter.mention);
    this.twitterPositiveMentions = String(data.twitter.positiveMention);
    this.twitterNegativeMentions = String(data.twitter.negativeMention);
  }

  presentPeers() {
    var data = JSON.parse(window.localStorage.getItem('peers') || "");
    this.peers =data.join(', ');
    this.peers = String(data.length);
    for (var i = 0; i < data.length; i++) {
      if (i != 0) {
        let comma = document.createElement("span");
        comma.innerHTML = ", ";
        this.peerlist.nativeElement.appendChild(comma);
      }
      let peer = document.createElement("a");
      peer.innerHTML = data[i];
      peer.href = "javascript:void(0);";
      peer.onclick = () => {
        this.handlePeerSearch(peer.innerHTML);
      }
      this.peerlist.nativeElement.appendChild(peer);
    }
  }

  presentEarnings() {
    var data = JSON.parse(window.localStorage.getItem('earnings') || "");
    var xCategories = [];
    var xActual = [];
    var xEstimate = [];
    for (var i = 0; i < data.length; i++) {
      xCategories.push(data[i].period + "<br/>Surprise: " + String(data[i].surprise));
      xActual.push(data[i].actual);
      xEstimate.push(data[i].estimate);
    }
    this.earningsChartOptions = {
      title: {
        text: "Historical EPS Surprises"
      },
      xAxis: {
        categories: xCategories
      },
      yAxis: {
        title: {
          text: 'Quantity EPS'
        },
      },
      tooltip: {
        shared: true,
      },
      series: [
        {
          data: xActual,
          name: 'Actual',
          type: 'spline'
        },
        {
          data: xEstimate,
          name: 'Estimate',
          type: 'spline'
        }
      ]
    };
  }

  presentSummaryChart() {
    var data = JSON.parse(window.localStorage.getItem('summaryChart') || "");
    var color = 'green';
    if (Number(this.dp) < 0) {
      color = 'red';
    }
    if (data.s != null && data.s == 'ok') {
      for (var i = 0; i < data.t.length; i++) {
        // TODO: fix timezone issue
        data.t[i] = (data.t[i] - 28800) * 1000;
      }
    }
    this.summaryChartOptions = {
      time: {
        timezone: 'America/Los_Angeles'
      },
      title: {
        text: `${this.ticker} Hourly Price Variation`
      },
      xAxis: {
        type: 'datetime',
        labels: {
          enabled: true,
          format: '{value:%H:%M}',
        },
        categories: data.t,
        tickInterval: 10
      },
      yAxis: {
        title: {
          text: ''
        },
        opposite: true
      },
      series: [{
        showInLegend: false,
        data: data.c,
        type: 'line',
        name: 'Price',
        marker: {
          radius: 0,
          lineWidth: 1,
        },
        color: color
      }]
    };
  }

  presentHistoryChart() {
    //var data = window.localStorage.getItem('historyChart') || "";
  }

  // data load flags checking
  checkDataLoadFlags() {
    if(this.profileLoadedFlag &&
      this.latestLoadedFlag &&
      this.newsLoadedFlag &&
      this.recommendationLoadedFlag &&
      this.socialSentimentLoadedFlag &&
      this.peersLoadedFlag &&
      this.earningsLoadedFlag &&
      this.summaryChartLoadedFlag &&
      this.historyChartLoadedFlag) {
      this.context.setValidDataPresentFlag(true);
      this.resetDataLoadFlags();
    }
  }

  resetDataLoadFlags() {
    this.profileLoadedFlag = false;
    this.latestLoadedFlag = false;
    this.newsLoadedFlag = false;
    this.recommendationLoadedFlag = false;
    this.socialSentimentLoadedFlag = false;
    this.peersLoadedFlag = false;
    this.earningsLoadedFlag = false;
    this.summaryChartLoadedFlag = false;
    this.historyChartLoadedFlag = false;
  }
}
