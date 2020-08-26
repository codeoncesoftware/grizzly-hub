import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SwaggerListComponent } from './swagger-list.component';

describe('SwaggerListComponent', () => {
  let component: SwaggerListComponent;
  let fixture: ComponentFixture<SwaggerListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SwaggerListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SwaggerListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
