import { AfterViewChecked, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-news-modal',
  templateUrl: './news-modal.component.html',
  styleUrls: ['./news-modal.component.css']
})
export class NewsModalComponent implements OnInit, AfterViewChecked {
  @Input() source: String = "";
  @Input() date: String = "";
  @Input() title: string = "";
  @Input() description: String = "";
  @Input() url: string = "";

  @ViewChild('url') urlHref!: ElementRef;
  @ViewChild('twitterhref') twitterhref!: ElementRef;
  @ViewChild('fbhref') fbhref!: ElementRef;

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit(): void {
  }

  ngAfterViewChecked(): void {
      this.urlHref.nativeElement.href = this.url;
      this.twitterhref.nativeElement.href = `https://twitter.com/intent/tweet?text=${encodeURIComponent(this.title)}&url=${encodeURIComponent(this.url)}`;
      this.fbhref.nativeElement.href = `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(this.url)}&amp;src=sdkpreparse`;
  }

}