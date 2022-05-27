import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertService } from 'src/app/service/alert/alert.service';
import { GlobalService } from 'src/app/service/global/global.service';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-load-specific-user',
  templateUrl: './load-specific-user.component.html',
  styleUrls: ['./load-specific-user.component.css']
})
export class LoadSpecificUserComponent implements OnInit {

  form: FormGroup;
  submitted: boolean = false;
  roleList: Array<string> = ["ADMIN", "USER"];
  username: string;

  roles: string;
  email: string;
  createDateTime: string;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private userService: UserService,
    private alertService: AlertService) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({

      username: [''],
      roles: [],
      email: [],
      createDateTime: []
    })
    this.username = this.route.snapshot.params['username'];
    this.getUserDeatils();
  }

  getUserDeatils() {

    this.userService.findUserUsingUserName(this.username).subscribe(
      data => {
        this.username = data.user.username;
        this.roles = data.user.roles;
        this.email = data.user.email;
        this.createDateTime = data.user.createDateTime;
      },
      error => {
        console.log(error);
        this.alertService.error('Some error has occured!!!', { keepAfterRouteChange: false });
      }
    )
  }

}
