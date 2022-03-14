import { Component, OnInit } from '@angular/core';
import { ContextService } from '../context.service';

import * as Highstock from 'highcharts/highstock';
import HC_more from 'highcharts/highcharts-more';
import HC_exporting from 'highcharts/modules/exporting';
import HC_drag_pane from 'highcharts/modules/drag-panes'
import HC_indicators_all from 'highcharts/indicators/indicators-all';
import HC_vbp from 'highcharts/indicators/volume-by-price';
HC_more(Highstock);
HC_exporting(Highstock);
HC_drag_pane(Highstock);
HC_indicators_all(Highstock);
HC_vbp(Highstock);

@Component({
  selector: 'app-search-chart',
  templateUrl: './search-chart.component.html',
  styleUrls: ['./search-chart.component.css']
})
export class SearchChartComponent implements OnInit {

  ticker: string = '';
  ohlc: number[][] = [[1584316800000,93.89994,98.97398,88.434,89.014],[1584403200000,88.002,94.37,79.2,86.03999999999999]];
  volume: number[][] = [[1584316800000,102447320],[1584403200000,119972900]];

  highstock: typeof Highstock = Highstock;
  historyChartOptions!: Highstock.Options;

  constructor(private context: ContextService) { 
    console.log("constructor called!!");
    this.ticker = context.ticker;
    this.ohlc = this.context.ohlc;
    this.volume = this.context.volume;
  }

  ngOnInit(): void {
    this.historyChartOptions = {
      rangeSelector: {
        allButtonsEnabled: true,
        selected: 2
      },
      title: {
        text: this.ticker + ' Historical'
      },
      subtitle: {
        text: 'With SMA and Volume by Price technical indicators'
      },
      yAxis: [{
        startOnTick: false,
        endOnTick: false,
        labels: {
          align: 'right',
          x: -3
        },
        title: {
          text: 'OHLC'
        },
        height: '60%',
        lineWidth: 2,
        resize: {
          enabled: true
        }
      }, {
        labels: {
          align: 'right',
          x: -3
        },
        title: {
          text: 'Volume'
        },
        top: '65%',
        height: '35%',
        offset: 0,
        lineWidth: 2
      }],
      tooltip: {
        split: true
      },
      plotOptions: {
        series: {
          dataGrouping: {
            enabled: false
          }
        }
      },
      series: [{
        type: 'candlestick',
        name: this.ticker,
        id: 'aapl',
        zIndex: 2,
        data: this.ohlc
      }, {
        type: 'column',
        name: 'Volume',
        id: 'volume',
        data: this.volume,
        yAxis: 1
      }, {
        type: 'vbp',
        linkedTo: 'aapl',
        params: {
          volumeSeriesID: 'volume'
        },
        dataLabels: {
          enabled: false
        },
        zoneLines: {
          enabled: false
        }
      }, {
        type: 'sma',
        linkedTo: 'aapl',
        zIndex: 1,
        marker: {
          enabled: false
        }
      }]
    };
  }

}
