import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-search-specific-user',
  templateUrl: './search-specific-user.component.html',
  styleUrls: ['./search-specific-user.component.css']
})
export class SearchSpecificUserComponent implements OnInit {

  message:string = '';
  invalidInput:boolean = false;

  form: FormGroup;
  submitted:boolean = false;

  constructor(private router: Router,
    private formBuilder: FormBuilder,
    private userService: UserService) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: ['', Validators.required]
    });

  }

  // convenience getter for easy access to form fields
  get f() { return this.form.controls; }

  findAUserUsingUsername() {

    this.submitted = true;
    // stop here if form is invalid
    if (this.form.invalid) {
      this.invalidInput = false
      return;
    }

      this.userService.findUserUsingUserName(this.f.username.value).subscribe(
        data => {
          console.log(data)
          this.router.navigate(['/loadSpecificUser', this.f.username.value]);

        },
        error => {
          console.log(error)
          this.message = 'Invalid username!!!';
          this.invalidInput = true;
        }
      )
  }

}
