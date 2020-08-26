import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SwaggerContainerComponent } from './swagger-container.component';

describe('SwaggerContainerComponent', () => {
  let component: SwaggerContainerComponent;
  let fixture: ComponentFixture<SwaggerContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SwaggerContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SwaggerContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
