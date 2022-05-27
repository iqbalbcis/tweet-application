import { Component, OnInit } from '@angular/core';
import { AlertService } from 'src/app/service/alert/alert.service';
import { GlobalService } from 'src/app/service/global/global.service';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-all-users',
  templateUrl: './all-users.component.html',
  styleUrls: ['./all-users.component.css']
})
export class AllUsersComponent implements OnInit {

  userList: Array<string>;

  constructor(
    private userService: UserService,
    private globalService: GlobalService,
    private alertService: AlertService) { }

  ngOnInit(): void {

    this.getAllUsers();
  }

  getAllUsers() {
    // reset alerts on submit
    this.alertService.clear();

    this.userService.findAllUsers().subscribe(
      data => {
        console.log(data);
        this.userList = data.usersList;
      },
      error => {
        console.log(error);
        this.globalService.goToTop();
        this.alertService.error('Some error has occured!!!', { keepAfterRouteChange: false });
      }
    )
  }

  goToTheTop() {
    this.globalService.goToTop();
  }

}
