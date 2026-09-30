import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvoicesdashboardComponent } from './invoicesdashboard.component';

describe('InvoicesdashboardComponent', () => {
  let component: InvoicesdashboardComponent;
  let fixture: ComponentFixture<InvoicesdashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InvoicesdashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InvoicesdashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
