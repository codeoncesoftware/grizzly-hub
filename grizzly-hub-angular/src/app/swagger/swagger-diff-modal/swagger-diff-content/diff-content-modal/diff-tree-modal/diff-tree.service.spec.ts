import { TestBed } from '@angular/core/testing';

import { DiffTreeService } from './diff-tree.service';

describe('DiffTreeService', () => {
  let service: DiffTreeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DiffTreeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
