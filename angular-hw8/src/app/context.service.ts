import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ContextService {
  // user search input text, used to pass from search bar to search component
  searchInput: String = "";
  // used to cache the current search route symbol
  searchSymbol: string = "";
  searchSymbolSubject: Subject<String> = new Subject();

  validDataPresentSubject: Subject<boolean> = new Subject();
  validDataPresent: boolean = false;
  clearContentFlag: boolean = false;

  cardSwitchFlagSubject: Subject<boolean> = new Subject();
  cardSwitchFlag: boolean = false;
  cardNavbarSwitchFlagSubject: Subject<boolean> = new Subject();
  cardNavbarSwitchFlag: boolean = false;

  // for charts use
  ticker: string = '';
  ohlc!: number[][];
  volume!: number[][];

  constructor() { 
    this.searchInput = "";
    this.searchSymbol = 'home';
  }

  getSearchInput() {
    return this.searchInput;
  }

  setSearchInput(text: String) {
    this.searchInput = text;
  }

  getSearchSymbol() {
    return this.searchSymbol;
  }

  setSearchSymbol(text: string) {
    this.searchSymbol = text;
    this.searchSymbolSubject.next(this.searchSymbol);
  }

  getClearContentFlag() {
    return this.clearContentFlag;
  }

  setClearContentFlag(flag: boolean) {
    this.clearContentFlag = flag;
  }

  setValidDataPresentFlag(flag: boolean) {
    this.validDataPresent = flag;
    this.validDataPresentSubject.next(this.validDataPresent);
  }

  getValidDataPresentFlag() {
    return this.validDataPresent;
  }

  setCardSwitchFlag(flag: boolean) {
    this.cardSwitchFlag = flag;
    if (flag) {
      this.setValidDataPresentFlag(false);
    }
    this.cardSwitchFlagSubject.next(this.cardSwitchFlag);
  }

  setNavbarCardSwitchFlag(flag: boolean) {
    this.cardNavbarSwitchFlag = flag;
    this.cardNavbarSwitchFlagSubject.next(this.cardSwitchFlag);
  }
}
