import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { timer } from 'rxjs';

import { ContextService } from '../context.service';
import { DataService } from '../data.service';
import { TransactionService } from '../transaction.service';
import { TransactionModalComponent } from '../transaction-modal/transaction-modal.component';

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrls: ['./portfolio.component.css']
})
export class PortfolioComponent implements OnInit {

  stocklistEmptyAlert: boolean;
  balance: number;
  stockhold: {[key: string]: any};
  stocklist: any[];

  alertTicker: string = '';
  buyAlertFlag: boolean = false;
  sellAlertFlag: boolean = false;

  constructor(
    private router: Router,
    private context: ContextService,
    private dataService: DataService,
    private transactionService: TransactionService,
    private modalService: NgbModal
  ) {
    this.stocklist = [];
    this.stocklistEmptyAlert = true;
    this.balance = this.transactionService.getBalance();
    this.stockhold = this.transactionService.getStockhold();
    for (var ticker in this.stockhold) {
      var item = Object(this.stockhold[ticker]);
      item.ticker = ticker;
      item.avg = Math.round((item.total / item.quantity) * 100) / 100;
      item.total = Number(item.total).toFixed(2);
      item.avg = item.avg.toFixed(2);
      item.quantityStr = item.quantity.toFixed(2);
      item.change = '0.00';
      item.current = item.avg;
      item.value = item.total;
      item.color = "text-success"; // text color
      this.stocklist.push(item);
      console.log(item);
      this.dataService.getLatestDataBySymbol(item.symbol);
    }
    this.stocklistEmptyAlert = this.stocklist.length == 0;
  }

  onCardClick(symbol: string) {
    this.context.setSearchSymbol(symbol);
    this.context.setCardSwitchFlag(true);
    this.context.setNavbarCardSwitchFlag(true);
    this.router.navigateByUrl('/search/'+symbol);
  }

  startBuyTransaction(ticker: string, symbol: string, price: string) {
    this.alertTicker = ticker;
    let modalRef = this.modalService.open(TransactionModalComponent);
    modalRef.componentInstance.mode = 'Buy';
    modalRef.componentInstance.symbol = symbol;
    modalRef.componentInstance.ticker = ticker;
    modalRef.componentInstance.price = Number(price);
    modalRef.result.then(data => {
      if (data == 'success') {
        this.refreshStockList();
        this.buyAlertFlag = true;
        const tsrc = timer(5000);
        tsrc.subscribe(() => {
          this.buyAlertFlag = false;
        });
      }
    }).catch(() => {});
  }

  startSellTransaction(ticker: string, price: string) {
    this.alertTicker = ticker;
    let modalRef = this.modalService.open(TransactionModalComponent);
    modalRef.componentInstance.mode = 'Sell';
    modalRef.componentInstance.ticker = ticker;
    modalRef.componentInstance.price = Number(price);
    modalRef.result.then(data => {
      if (data == 'success' || data == 'zero') {
        this.refreshStockList();
        this.sellAlertFlag = true;
        const tsrc = timer(5000);
        tsrc.subscribe(() => {
          this.sellAlertFlag = false;
        });
      }
    }).catch(() => {});
  }

  ngOnInit(): void {
    this.context.setPortfolioTab();
    this.dataService.latestDataBySymbolReadySubject.subscribe(data => {
      if (data == null || data == '') {
        return;
      }
      var dataobj = JSON.parse(data);
      for (var i = 0; i < this.stocklist.length; i++) {
        if (dataobj.symbol == this.stocklist[i].symbol) {
          this.stocklist[i].price = dataobj.c;
          var change = Math.round((Number(dataobj.c) - Number(this.stocklist[i].price))) / 100;
          if (change < 0) {
            this.stocklist[i].positive = false;
            this.stocklist[i].color = "text-danger";
          } else {
            this.stocklist[i].positive = true;
            this.stocklist[i].color = "text-success";
          }
          this.stocklist[i].change = change.toFixed(2);
          this.stocklist[i].value = (Number(dataobj.c) * this.stocklist[i].quantity).toFixed(2);
        }
      }
    });
  }

  refreshStockList() {
    this.stocklist = [];
    this.stocklistEmptyAlert = true;
    this.balance = this.transactionService.getBalance();
    this.stockhold = this.transactionService.getStockhold();
    for (var ticker in this.stockhold) {
      var item = Object(this.stockhold[ticker]);
      item.ticker = ticker;
      item.avg = Math.round((item.total / item.quantity) * 100) / 100;
      item.total = Number(item.total).toFixed(2);
      item.avg = item.avg.toFixed(2);
      item.quantityStr = item.quantity.toFixed(2);
      item.change = '0.00';
      item.current = item.avg;
      item.value = item.total;
      item.color = "text-success"; // text color
      this.stocklist.push(item);
      console.log(item);
      //dataService.getLatestDataBySymbol(item.symbol);
    }
    this.stocklistEmptyAlert = this.stocklist.length == 0;
  }

}
