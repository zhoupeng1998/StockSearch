import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ContextService {
  // user search input text, used to pass from search bar to search component
  searchInput: String = "";
  // used to cache the current search route symbol
  searchSymbol: String = "";
  searchSymbolSubject: Subject<String> = new Subject();

  validDataPresentSubject: Subject<boolean> = new Subject();
  validDataPresent: boolean = false;
  clearContentFlag: boolean = false;

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

  setSearchSymbol(text: String) {
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
}
