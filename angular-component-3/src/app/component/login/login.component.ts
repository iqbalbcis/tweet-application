import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthenticationServiceService } from 'src/app/service/authentication/authentication-service.service';
import { SetInterval } from '../menuandheader/menuandheader.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {

  errorMessage = 'Invalid Credentials'
  invalidLogin = false

  form: FormGroup;
  submitted = false;

  constructor(private router: Router,
    private authenticationService: AuthenticationServiceService,
    private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: ['', Validators.required],
      passWord: ['', [Validators.required]]
    });

  }

  // convenience getter for easy access to form fields
  get f() { return this.form.controls; }

  handleJWTAuthLogin() {

    this.submitted = true;
    // stop here if form is invalid
    if (this.form.invalid) {
      this.invalidLogin = false
      return;
    }

    this.authenticationService.loginService(this.f.username.value, this.f.passWord.value)
      .subscribe(
        data => {
          console.log(data)
          this.invalidLogin = false;

          new SetInterval(this.authenticationService).refreshToken();
          this.router.navigate(['/post-new-tweet']);
        },
        error => {
          console.log(error)
          this.submitted = false;
          this.f.passWord.setValue('');
          this.invalidLogin = true;
        }
      )
  }
}
