import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertService } from 'src/app/service/alert/alert.service';
import { GlobalService } from 'src/app/service/global/global.service';
import { UserService } from 'src/app/service/user/user.service';
import { ConfirmedValidator } from '../register/register.component';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  form: FormGroup;
  submitted: boolean = false;
  username: string;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private userService: UserService,
    private globalService: GlobalService,
    private alertService: AlertService) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({

      passWord: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    }, {
      validator: ConfirmedValidator('passWord', 'confirmPassword')
    })
    this.username = this.route.snapshot.params['username'];
  }

  // convenience getter for easy access to form fields
  get f() { return this.form.controls; }


  resetPassword() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.form.invalid) {
      this.globalService.goToTop();
      this.alertService.error('Some required fields were missing or contain invalid data!!!', { keepAfterRouteChange: false });
      return;
    }

    this.userService.resetPassword(this.username, this.form.value).subscribe(
      data => {
        console.log(data)
        this.router.navigate(['/successmessage']);
      },
      error => {
        console.log(error);
        this.globalService.goToTop();
        this.alertService.error( error.error.message +'!!!', { keepAfterRouteChange: false });
      })
  }
}
