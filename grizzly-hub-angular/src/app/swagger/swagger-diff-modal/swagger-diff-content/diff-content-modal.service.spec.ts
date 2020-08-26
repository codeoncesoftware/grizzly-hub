import { TestBed } from '@angular/core/testing';

import { DiffContentModalService } from './diff-content-modal.service';

describe('DiffContentModalService', () => {
  let service: DiffContentModalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DiffContentModalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
