import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyHubComponent } from './my-hub.component';

describe('MyHubComponent', () => {
  let component: MyHubComponent;
  let fixture: ComponentFixture<MyHubComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyHubComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyHubComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
