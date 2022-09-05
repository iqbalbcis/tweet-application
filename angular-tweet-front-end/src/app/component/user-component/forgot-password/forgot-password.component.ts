import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  message:string = '';
  input:boolean = false;

  form: FormGroup;
  submitted:boolean = false;

  constructor(private router: Router,
    private formBuilder: FormBuilder,
    private userService: UserService) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      email: ['', Validators.required]
    });

  }

  // convenience getter for easy access to form fields
  get f() { return this.form.controls; }

  forgotPassword() {

    this.submitted = true;
    // stop here if form is invalid
    if (this.form.invalid) {
      this.input = false
      return;
    }

      this.userService.findAUserUsingEmailForgotPassword(this.f.email.value).subscribe(
        data => {
          console.log(data)
          this.message = 'An email has been sent to reset password';
          this.input = true;
          this.submitted = false;
          this.f.email.setValue('');
        },
        error => {
          console.log(error)
          this.message = 'Invalid email!!!';
          this.input = true;
        }
      )
  }

}
