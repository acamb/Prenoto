import { TestBed } from '@angular/core/testing';

import { PostiRiservatiService } from './posti-riservati.service';

describe('PostiRiservatiService', () => {
  let service: PostiRiservatiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostiRiservatiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
