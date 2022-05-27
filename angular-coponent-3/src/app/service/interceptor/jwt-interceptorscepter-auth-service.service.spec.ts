import { TestBed } from '@angular/core/testing';

import { HTTPINTERCEPTORScepterAuthServiceService } from './jwt-interceptorscepter-auth-service.service';

describe('HTTPINTERCEPTORScepterAuthServiceService', () => {
  let service: HTTPINTERCEPTORScepterAuthServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HTTPINTERCEPTORScepterAuthServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
