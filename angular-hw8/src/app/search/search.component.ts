import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { interval, Subscription, timer } from 'rxjs';
import { ContextService } from '../context.service';
import { DataService } from '../data.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit, OnDestroy {
  inputText: String = "";
  noInputFlag: boolean = false;
  validSymbolFlag: boolean = false;
  invalidSymbolFlag: boolean = false;
  loadingFlag: boolean = false;
  clearFlag: boolean = false; // used by onNotify method only!

  // data loading flags
  profileLoadedFlag: boolean = false;
  latestLoadedFlag: boolean = false;
  summaryChartLoadedFlag: boolean = false;

   // TODO: testing only, delete these
  testText1: String = "";
  testText2: String = "";
  testText3: String = "";

  intervalSubscription: Subscription = new Subscription();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private context: ContextService,
    private dataService: DataService) { }

  // called only when switch from /watchlist or /portfolio
  ngOnInit(): void {
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
      this.presentAll();
      this.validSymbolFlag = true;
    }

    // initialize timer for periodic update
    const updateTimer = timer(0, 15000);
    this.intervalSubscription = updateTimer.subscribe(() => {
      if (this.validSymbolFlag) {
        // TODO: do periodic update
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
        this.presentProfile();
        this.checkDataLoadFlags();
      } else {
        this.context.setValidDataPresentFlag(false);
      }
    });

    this.dataService.latestDataReadySubject.subscribe(() => {
      this.latestLoadedFlag = true;
      this.presentLatest();
      this.checkDataLoadFlags();
    });

    this.dataService.newsDataReadySubject.subscribe(() => {
      this.presentNews();
    });

    this.dataService.recommendationDataReadySubject.subscribe(() => {
      this.presentRecommendation();
    });

    this.dataService.socialSentimentDataReadySubject.subscribe(() => {
      this.presentSocialSentiment();
    });

    this.dataService.peersDataReadySubject.subscribe(() => {
      this.presentPeers();
    });

    this.dataService.earningsDataReadySubject.subscribe(() => {
      this.presentEarnings();
    });

    this.dataService.summaryChartReadySubject.subscribe(() => {
      this.summaryChartLoadedFlag = true;
      this.presentSummaryChart();
      this.checkDataLoadFlags();
    });

    this.dataService.historyChartReadySubject.subscribe(() => {
      this.presentHistoryChart();
    });
  }

  ngOnDestroy(): void {
    this.intervalSubscription.unsubscribe();
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
    //var data = window.localStorage.getItem('profile') || "";
  }

  presentLatest() {
    //var data = window.localStorage.getItem('latest') || "";
    this.testText2 = window.localStorage.getItem('latest') || "";
  }

  presentNews() {
    //var data = window.localStorage.getItem('news') || "";
  }

  presentRecommendation() {
    //var data = window.localStorage.getItem('recommendation') || "";
  }

  presentSocialSentiment() {
    //var data = window.localStorage.getItem('social') || "";
  }

  presentPeers() {
    //var data = window.localStorage.getItem('peers') || "";
  }

  presentEarnings() {
    //var data = window.localStorage.getItem('earnings') || "";
  }

  presentSummaryChart() {
    //var data = window.localStorage.getItem('summaryChart') || "";
    this.testText3 = window.localStorage.getItem('summaryChart') || "";
  }

  presentHistoryChart() {
    //var data = window.localStorage.getItem('historyChart') || "";
  }

  // data load flags checking
  checkDataLoadFlags() {
    if(this.profileLoadedFlag && this.latestLoadedFlag && this.summaryChartLoadedFlag) {
      this.context.setValidDataPresentFlag(true);
      this.resetDataLoadFlags();
    }
  }

  resetDataLoadFlags() {
    this.profileLoadedFlag = false;
    this.latestLoadedFlag = false;
    this.summaryChartLoadedFlag = false;
  }
}
