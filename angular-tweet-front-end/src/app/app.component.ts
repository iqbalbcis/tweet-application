import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { AUTHENTICATED_USER } from './app.constants';
import { AuthenticationServiceService } from './service/authentication/authentication-service.service';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  
  userActivity;
  userInactive: Subject<any> = new Subject()

  constructor(
    private router: Router,
    private authenticationServiceService: AuthenticationServiceService) { }

  ngOnInit(): void {

    this.setTimeout();
    this.userInactive.subscribe(() => {
      this.logout();
    });
    this.logoutFromAllTabIfOneIsCompleted();
  }

  setTimeout() {
    this.userActivity = setTimeout(() => {
      if (this.authenticationServiceService.isUserLoggedIn()) {
        this.userInactive.next(undefined);
        console.log('logged out');
      }
    }, 1000 * 60 * 10); // 10 minutes no activity logout automatically
  }

  logout() {
    this.authenticationServiceService.logoutService();
    this.router.navigate(['/login']);

  }

  logoutFromAllTabIfOneIsCompleted() {
    window.addEventListener('storage', (event) => {
      if (event.storageArea == localStorage) {
        let token = localStorage.getItem(AUTHENTICATED_USER);
        if (token == undefined) {
          // Perform logout
          //Navigate to login/home
          this.logout();
        }
      }
    });
  }

  // @HostListener('window:mousemove') refreshUserState() {
  //   clearTimeout(this.userActivity);
  //   this.setTimeout();
  // }
  @HostListener('document:mousemove', ['$event'])
  @HostListener('document:mousedown', ['$event'])
  @HostListener('document:mouseup', ['$event'])
  @HostListener('document:touchmove', ['$event'])
  refreshUserState() {
    clearTimeout(this.userActivity);
    this.setTimeout();
  }
}
