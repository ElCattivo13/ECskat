import { TestBed } from '@angular/core/testing';

import { SpielService } from './spiel.service';

describe('SpielService', () => {
  let service: SpielService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpielService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
