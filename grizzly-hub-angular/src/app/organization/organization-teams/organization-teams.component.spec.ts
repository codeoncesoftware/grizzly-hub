import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OrganizationTeamsComponent } from './organization-teams.component';

describe('OrganizationTeamsComponent', () => {
  let component: OrganizationTeamsComponent;
  let fixture: ComponentFixture<OrganizationTeamsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OrganizationTeamsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrganizationTeamsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
