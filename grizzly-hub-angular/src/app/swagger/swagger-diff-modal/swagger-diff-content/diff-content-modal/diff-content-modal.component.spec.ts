import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiffContentModalComponent } from './diff-content-modal.component';

describe('DiffContentModalComponent', () => {
  let component: DiffContentModalComponent;
  let fixture: ComponentFixture<DiffContentModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiffContentModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiffContentModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
