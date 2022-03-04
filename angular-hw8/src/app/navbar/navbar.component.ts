import { Component, OnInit } from '@angular/core';
import { ContextService } from '../context.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isSearchTabFlag: boolean = true;
  validDataPresent: boolean = false;

  constructor(private context: ContextService) { }

  ngOnInit(): void {
    this.context.validDataPresentSubject.subscribe(flag => {
      this.validDataPresent = flag;
      if (this.isSearchTabFlag) {
        this.setSearchTab();
      }
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
