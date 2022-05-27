import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AlertService } from 'src/app/service/alert/alert.service';
import { TweetService } from 'src/app/service/tweet/tweet.service';

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css']
})
export class MessageComponent implements OnInit {

  form: FormGroup;
  submitted: boolean = false;

  message:string = '';
  invalidInput:boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private tweetService: TweetService,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      text: ['', Validators.required]
    })
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.form.controls;
  }

  sendMessage() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.form.invalid) {
      //this.globalService.goToTop();
      return;
    }

    this.tweetService.sendMessage(this.f.text.value).subscribe(
      data => {
        console.log(data)
        this.submitted = false;
        this.f.text.setValue('');
        this.message = 'Sent successfully!!';
        this.invalidInput = true;
      },
      error => {
        console.log(error);
        this.message = 'Not successful!!';
      })
  }

}
