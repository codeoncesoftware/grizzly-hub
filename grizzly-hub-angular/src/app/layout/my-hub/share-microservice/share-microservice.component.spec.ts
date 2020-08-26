import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShareMicroserviceComponent } from './share-microservice.component';

describe('ShareMicroserviceComponent', () => {
  let component: ShareMicroserviceComponent;
  let fixture: ComponentFixture<ShareMicroserviceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShareMicroserviceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShareMicroserviceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
