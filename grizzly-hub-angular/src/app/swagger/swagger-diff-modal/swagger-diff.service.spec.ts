import { TestBed } from '@angular/core/testing';

import { SwaggerDiffService } from './swagger-diff.service';

describe('SwaggerDiffService', () => {
  let service: SwaggerDiffService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SwaggerDiffService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
