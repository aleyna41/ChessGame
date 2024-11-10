import { TestBed } from '@angular/core/testing';

import { LeaderboardServiceService } from './leaderboard-service.service';

describe('LeaderboardServiceService', () => {
  let service: LeaderboardServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LeaderboardServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
