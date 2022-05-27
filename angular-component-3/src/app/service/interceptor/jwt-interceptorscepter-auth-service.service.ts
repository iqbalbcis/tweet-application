import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthenticationServiceService } from '../authentication/authentication-service.service';


@Injectable({
  providedIn: 'root'
})
export class HTTPINTERCEPTORScepterAuthServiceService implements HttpInterceptor{

  constructor(
    private authenticationService : AuthenticationServiceService
  ) { }

  // need to put code in provide[] in app.module.ts
  intercept(request: HttpRequest<any>, next: HttpHandler) {

    let authHeaderString = this.authenticationService.getAuthenticatedToken();
    let username = this.authenticationService.getAuthenticatedUser()

    if(authHeaderString && username) {
      request = request.clone({
        setHeaders : {
            Authorization : authHeaderString
          }
        })
    }
    return next.handle(request);
  }
}
