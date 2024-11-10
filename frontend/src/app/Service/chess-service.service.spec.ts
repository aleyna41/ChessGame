import { TestBed } from '@angular/core/testing';

import { ChessServiceService } from './chess-service.service';

describe('ChessServiceService', () => {
  let service: ChessServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChessServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
