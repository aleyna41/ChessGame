import { TestBed } from '@angular/core/testing';

import { ExpImpServiceService } from './exp-imp-service.service';

describe('ExpImpServiceService', () => {
  let service: ExpImpServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpImpServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
