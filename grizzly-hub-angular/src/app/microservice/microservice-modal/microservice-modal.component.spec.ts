import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MicroserviceModalComponent } from './microservice-modal.component';

describe('MicroserviceModalComponent', () => {
  let component: MicroserviceModalComponent;
  let fixture: ComponentFixture<MicroserviceModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MicroserviceModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MicroserviceModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
