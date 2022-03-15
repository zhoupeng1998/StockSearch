import { formatNumber } from '@angular/common';
import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TransactionService } from '../transaction.service';

@Component({
  selector: 'app-transaction-modal',
  templateUrl: './transaction-modal.component.html',
  styleUrls: ['./transaction-modal.component.css']
})
export class TransactionModalComponent implements OnInit {
  @Input() mode: string = "";
  @Input() symbol: string = "";
  @Input() name: string = "";
  @Input() ticker: string = "";
  @Input() price: number = 0;
  balance: number;
  holding: number = 0;
  quantity: number = 0;
  total: number = 0;
  totalStr: string = '0';

  fractionalAlert: boolean = false;
  sufficientBuyAlert: boolean = false;
  sufficientSellAlert: boolean = false;
  disabled: boolean = true;

  constructor(
    private transactionService: TransactionService,
    public activeModal: NgbActiveModal) {
      this.balance = this.transactionService.balance;
    }

  ngOnInit(): void {
    this.holding = this.transactionService.getHoldingQuantity(this.ticker);
  }

  doTransaction() {
    if (this.mode == 'Buy') {
      this.transactionService.buyStock(this.ticker, this.symbol, this.name, this.quantity, this.total);
      this.activeModal.close('success');
    } else {
      this.holding -= this.quantity;
      var result = this.transactionService.sellStock(this.ticker, this.quantity, this.total);
      if (result) {
        if (this.holding == 0) {
          this.activeModal.close('zero');
        } else {
          this.activeModal.close('success');
        }
      } else {
        this.activeModal.close('error');
      }
    }
  }

  onValueChange() {
    if (this.quantity == null) {
      this.totalStr = "";
    } else {
      this.total = this.price * this.quantity;
      this.total = Math.round(this.total * 100) / 100;
      this.totalStr = this.total.toFixed(2);
    }
    this.disabled = false;
    if (this.quantity != null && !Number.isInteger(this.quantity)) {
      this.fractionalAlert = true;
      this.disabled = true;
    } else {
      this.fractionalAlert = false;
    }
    if (this.mode == 'Buy') {
      if (this.balance < this.total) {
        this.sufficientBuyAlert = true;
        this.disabled = true;
      } else {
        this.sufficientBuyAlert = false;
      }
    } else {
      if (this.holding < this.quantity) {
        this.sufficientSellAlert = true;
        this.disabled = true;
      } else {
        this.sufficientSellAlert = false;
      }
    }
    if (this.quantity == 0 || this.quantity == null) {
      this.disabled = true;
    }
  }

}
