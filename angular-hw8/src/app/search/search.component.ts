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

  ngOnInit(): void {
    const routeParams = this.route.snapshot.paramMap;
    this.inputText = String(routeParams.get('symbol'));
    if (this.inputText !== 'home') {
      console.log("load info in this condition");
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

  handleSearch() {
    this.inputText = this.context.getSearchInput();
    if (this.inputText == null || this.inputText.trim().length == 0) {
      this.validSymbolFlag = false;
      this.noInputFlag = true;
      this.context.setValidDataPresentFlag(false);
    } else {
      this.router.navigateByUrl('/search/'+this.inputText);
      this.noInputFlag = false;
      this.validSymbolFlag = true;
      // load info at this time
      this.context.setValidDataPresentFlag(true);
    }
  }

  closeNoInputAlert() {
    this.noInputFlag = false;
  }
}
