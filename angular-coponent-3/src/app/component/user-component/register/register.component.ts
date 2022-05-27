import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AlertService } from 'src/app/service/alert/alert.service';
import { GlobalService } from 'src/app/service/global/global.service';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  form: FormGroup;
  submitted: boolean = false;

  roleList: Array<string> = ["ADMIN", "USER"];

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private userService: UserService,
    private globalService: GlobalService,
    private alertService: AlertService) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({

      username: ['', Validators.required],
      passWord: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      roles: [null, Validators.required],
      email: ['', [Validators.required, Validators.pattern("([\\w.-]+)@([a-zA-Z0-9-]+)\\.([a-zA-Z]{2,8})(\\.[a-zA-Z]{2,6})?")]]
    }, {
      validator: ConfirmedValidator('passWord', 'confirmPassword')
    })
  }

  // convenience getter for easy access to form fields
  get f() { return this.form.controls; }


  registerUser() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.form.invalid) {
      this.globalService.goToTop();
      this.alertService.error('Some required fields were missing or contain invalid data!!!', { keepAfterRouteChange: false });
      return;
    }

    this.userService.registerUserDetails(this.form.value).subscribe(
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

export function ConfirmedValidator(controlName: string, matchingControlName: string) {
  return (formGroup: FormGroup) => {
    const control = formGroup.controls[controlName];
    const matchingControl = formGroup.controls[matchingControlName];
    if (matchingControl.errors && !matchingControl.errors.confirmedValidator) {
      return;
    }
    if (control.value !== matchingControl.value) {
      matchingControl.setErrors({ confirmedValidator: true });
    } else {
      matchingControl.setErrors(null);
    }
  }
}

