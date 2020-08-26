import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiffTreeModalComponent } from './diff-tree-modal.component';

describe('DiffTreeModalComponent', () => {
  let component: DiffTreeModalComponent;
  let fixture: ComponentFixture<DiffTreeModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiffTreeModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiffTreeModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
