import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SwaggerDiffComponent } from './swagger-diff.component';

describe('SwaggerDiffComponent', () => {
  let component: SwaggerDiffComponent;
  let fixture: ComponentFixture<SwaggerDiffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SwaggerDiffComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SwaggerDiffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
