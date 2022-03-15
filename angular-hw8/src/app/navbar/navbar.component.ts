import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContextService } from '../context.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isSearchTabFlag: boolean = true;
  validDataPresent: boolean = false;

  searchSymbol: String = "home";

  constructor(
    private router: Router,
    private context: ContextService) { }

  ngOnInit(): void {
    this.context.validDataPresentSubject.subscribe(flag => {
      this.validDataPresent = flag;
      if (this.isSearchTabFlag) {
        this.setSearchTab();
      }
    });

    // update search symbol upon user search
    this.context.searchSymbolSubject.subscribe(symbol => {
      this.searchSymbol = symbol;
    });

    this.context.cardNavbarSwitchFlagSubject.subscribe(flag => {
      if (flag) {
        this.setSearchTab();
      }
      this.context.cardNavbarSwitchFlag = false;
    });

    this.context.setWatchlistTabSubject.subscribe(() => {
      this.setWatchlistTab();
    });

    this.context.setPortfolioTabSubject.subscribe(() => {
      this.setPortfolioTab();
    })
  }

  setSearchTab() {
    document.getElementById('nav-watchlist')?.classList.remove('nav-activated');
    document.getElementById('nav-portfolio')?.classList.remove('nav-activated');
    if (this.validDataPresent) {
      document.getElementById('nav-search')?.classList.add('nav-activated');
    } else {
      document.getElementById('nav-search')?.classList.remove('nav-activated');
    }
    this.isSearchTabFlag = true;
  }

  setWatchlistTab() {
    document.getElementById('nav-search')?.classList.remove('nav-activated');
    document.getElementById('nav-watchlist')?.classList.add('nav-activated');
    document.getElementById('nav-portfolio')?.classList.remove('nav-activated');
    this.isSearchTabFlag = false;
  }
  
  setPortfolioTab() {
    document.getElementById('nav-search')?.classList.remove('nav-activated');
    document.getElementById('nav-watchlist')?.classList.remove('nav-activated');
    document.getElementById('nav-portfolio')?.classList.add('nav-activated');
    this.isSearchTabFlag = false;
  }

}
