import { TestBed } from '@angular/core/testing';

import { TeamHubService } from './team-hub.service';

describe('TeamHubService', () => {
  let service: TeamHubService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TeamHubService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
