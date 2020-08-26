import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubscriptionDetailsModalComponent } from './subscription-details-modal.component';

describe('SubscriptionDetailsModalComponent', () => {
  let component: SubscriptionDetailsModalComponent;
  let fixture: ComponentFixture<SubscriptionDetailsModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubscriptionDetailsModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubscriptionDetailsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
