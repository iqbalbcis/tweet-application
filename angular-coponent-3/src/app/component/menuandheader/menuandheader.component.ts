import { Component, Inject, OnInit} from '@angular/core';
import { TOKEN } from 'src/app/app.constants';
import { AuthenticationServiceService } from 'src/app/service/authentication/authentication-service.service';

@Component({
  selector: 'app-menuandheader',
  templateUrl: './menuandheader.component.html',
  styleUrls: ['./menuandheader.component.css']
})
export class MenuandheaderComponent implements OnInit {

  constructor(
    public authenticationService: AuthenticationServiceService) {

  }

  ngOnInit(): void {
    this.getUserName();
  }

  logout() {
    this.authenticationService.logoutService();
    //new SetInterval(this.authenticationService).start_stop_SetInterval();
  }

  getUserName()
  {
    return this.authenticationService.getAuthenticatedUser();
  }

}
export class SetInterval {

  //subscription: Subscription;

  constructor(
    private authenticationService: AuthenticationServiceService
  ) { }

  // start_stop_SetInterval() {
  //   const source = interval(1000*60*15); // 15 minutes
  //   this.subscription = source.subscribe(() => {

  //     if (sessionStorage.getItem(TOKEN) === null) {
  //       this.subscription && this.subscription.unsubscribe();
  //     }
  //     else {
  //       this.authenticationService.refreshToken().subscribe(
  //         data => {
  //           console.log(data);
  //           sessionStorage.removeItem(TOKEN);
  //           sessionStorage.setItem(TOKEN, `Bearer ${data.token}`);
  //         },
  //         error => {
  //           console.log(error);
  //         }
  //       )
  //     }
  //   });
  // }

  refreshToken() {
    if (sessionStorage.getItem(TOKEN) !== null) {
      this.authenticationService.refreshToken().subscribe(
        data => {
          console.log(data);
          sessionStorage.removeItem(TOKEN);
          sessionStorage.setItem(TOKEN, `Bearer ${data.token}`);
        },
        error => {
          console.log(error);
        }
      )
    }
  }


}
