import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchChartComponent } from './search-chart.component';

describe('SearchChartComponent', () => {
  let component: SearchChartComponent;
  let fixture: ComponentFixture<SearchChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchChartComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
