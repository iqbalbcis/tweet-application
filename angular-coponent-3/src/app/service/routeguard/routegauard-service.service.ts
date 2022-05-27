import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthenticationServiceService } from '../authentication/authentication-service.service';


@Injectable({
  providedIn: 'root'
})
export class RoutegauardServiceService implements CanActivate{

  constructor(
    private authenticationService: AuthenticationServiceService,
    private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot)
  {
    if (this.authenticationService.isUserLoggedIn())
      return true;

    this.router.navigate(['login']);

    return false;
  }
}
