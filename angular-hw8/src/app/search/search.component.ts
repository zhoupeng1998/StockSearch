import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ContextService } from '../context.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  inputText: String = "";
  // NOTE: change these flags into "false" when testing the program
  noInputFlag: boolean = false;
  validSymbolFlag: boolean = true;
  invalidSymbolFlag: boolean = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private context: ContextService) { }

  // called only when switch from /watchlist or /portfolio
  ngOnInit(): void {
    const routeParams = this.route.snapshot.paramMap;
    //this.inputText = String(routeParams.get('symbol'));
    this.inputText = this.context.getSearchSymbol();
    if (this.inputText !== 'home') {
      this.context.setSearchInput(this.inputText);
      this.handleSearch();
    } else {

    }
  }

  onNotify() {
    if (this.context.getClearContentFlag()) {
      this.noInputFlag = false;
      this.validSymbolFlag = false;
      this.invalidSymbolFlag = false;
      this.context.setClearContentFlag(false);
      this.context.setValidDataPresentFlag(false);
    } else {
      this.handleSearch();
    }
  }

  // TODO: handle input is "home"
  handleSearch() {
    this.inputText = this.context.getSearchInput();
    if (this.inputText == null || this.inputText.trim().length == 0) {
      this.validSymbolFlag = false;
      this.invalidSymbolFlag = false;
      this.noInputFlag = true;
      this.context.setValidDataPresentFlag(false);
      this.context.setSearchSymbol('home');
    } else {
      this.router.navigateByUrl('/search/'+this.inputText);
      this.noInputFlag = false;
      this.validSymbolFlag = true;
      // load info at this time

      // load info success?
      this.context.setValidDataPresentFlag(true);
      this.context.setSearchSymbol(this.inputText);
    }
  }

  closeNoInputAlert() {
    this.noInputFlag = false;
  }
}
