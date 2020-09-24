import { TestBed } from '@angular/core/testing';

import { PasswordTemporaneaGuard } from './password-temporanea.guard';

describe('PasswordTemporaneaGuard', () => {
  let guard: PasswordTemporaneaGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(PasswordTemporaneaGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
