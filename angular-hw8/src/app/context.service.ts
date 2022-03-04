import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ContextService {
  searchInput: String = "";
  clearContentFlag: boolean = false;

  validDataPresentSubject: Subject<boolean> = new Subject();
  validDataPresent: boolean = false;

  constructor() { 
    this.searchInput = "";
  }

  getSearchInput() {
    return this.searchInput;
  }

  setSearchInput(text: String) {
    this.searchInput = text;
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
}
