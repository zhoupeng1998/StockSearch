import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { map, Observable, startWith, of, debounceTime, finalize } from 'rxjs';

import { ContextService } from '../context.service';

export interface AutocompleteCompany {
  symbol: string;
  description: string;
}

@Component({
  selector: 'app-searchbar',
  templateUrl: './searchbar.component.html',
  styleUrls: ['./searchbar.component.css']
})
export class SearchbarComponent implements OnInit {
  @Output() notify = new EventEmitter();

  formCtrl = new FormControl();
  dummyCtrl = new FormControl();
  resultCompanies: Observable<any[]>;
  loading: boolean;
  autocompleteEnabled: boolean = true;
  companies: any[];

  constructor(
    private router: Router, 
    private http: HttpClient,
    private context : ContextService) {

    this.loading = false;
    this.companies = [];
    
    // fetch autocomplete result
    this.formCtrl.valueChanges.pipe(
      debounceTime(300),
    )
    .subscribe(val => {
      this.companies = [];
      this.dummyCtrl.setValue("");
      if (!val || val.length == 0) {
        this.loading = false;
        this.companies = [];
        // simulate a value change!
        this.dummyCtrl.setValue(" ");
        this.dummyCtrl.setValue("");
      } else {
        this.loading = true;
        var url = "http://localhost/api/autocomplete/" + val.trim();
        this.http.get<any[]>(url)
        .subscribe(res => {
          this.companies = res;
          this.dummyCtrl.setValue(val);
          this.loading = false;
        });
      }
    });
    
    this.resultCompanies = this.dummyCtrl.valueChanges.pipe(
      map(company => this.companies.slice())
    );
    
  }

  ngOnInit(): void {
  }

  onOptionClick(value: any) {
    if (value) {
      this.context.setSearchInput(value);
      this.notify.emit();
    }
    return value;
  }

  onFormSubmit(event: Event) {
    event.preventDefault();
    this.onSearchClick();
  }

  onSearchClick() {
    this.context.setSearchInput(this.formCtrl.value);
    this.notify.emit();
  }

  onClearClick() {
    this.formCtrl.setValue("");
    this.context.setClearContentFlag(true);
    this.context.setSearchInput(""); // TODO: decide whether to keep this line
    this.context.setValidDataPresentFlag(false);
    this.context.setSearchSymbol('home');
    this.context.cardSwitchFlag = false;
    this.context.cardNavbarSwitchFlag = false;
    this.notify.emit();
    this.router.navigate(['/']);
  }

  onSubmit() {
    //this.notify.emit();
    //this.router.navigateByUrl('/search/'+this.dummyCtrl.value);
  }

}
