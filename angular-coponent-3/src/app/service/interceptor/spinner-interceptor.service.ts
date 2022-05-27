import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { SpinnerServiceService } from '../spinner/spinner-service.service';

@Injectable({
  providedIn: 'root'
})
export class SpinnerInterceptorService implements HttpInterceptor {

  constructor(private spinnerService: SpinnerServiceService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    this.spinnerService.requestStarted();
    return this.handler(next, request);
  }

  handler(next, request)
  {
    return next.handle(request)
      .pipe(
        tap(
          (event) =>{
            if(event instanceof HttpResponse)
            {
              this.spinnerService.requestEnded();
            }
          },
          (error: HttpErrorResponse) => {
            this.spinnerService.resetSpinner();
            throw error;
          }
        )
      );
  }

}
