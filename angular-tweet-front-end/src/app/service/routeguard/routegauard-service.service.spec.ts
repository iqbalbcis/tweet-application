import { TestBed } from '@angular/core/testing';

import { RoutegauardServiceService } from './routegauard-service.service';

describe('RoutegauardServiceService', () => {
  let service: RoutegauardServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoutegauardServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
