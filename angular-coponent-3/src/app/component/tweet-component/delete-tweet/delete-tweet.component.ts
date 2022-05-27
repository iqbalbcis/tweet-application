import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AlertService } from 'src/app/service/alert/alert.service';
import { TweetService } from 'src/app/service/tweet/tweet.service';

@Component({
  selector: 'app-delete-tweet',
  templateUrl: './delete-tweet.component.html',
  styleUrls: ['./delete-tweet.component.css']
})
export class DeleteTweetComponent implements OnInit {

  message:string = '';
  invalidInput:boolean = false;

  form: FormGroup;
  submitted:boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private tweetService: TweetService,
    private alertService: AlertService) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      tweetId: ['', Validators.required]
    });

  }

  // convenience getter for easy access to form fields
  get f() { return this.form.controls; }

  deleteDataUsingTweetId() {

    this.submitted = true;
    this.alertService.clear();
    // stop here if form is invalid
    if (this.form.invalid) {
      this.invalidInput = false
      return;
    }
    this.tweetService.deleteTweetDetails(this.f.tweetId.value).subscribe(
      data => {
        console.log(data)
        this.message = data.message;
        this.invalidInput = true
        this.submitted = false;
        this.f.tweetId.setValue('');
      },
      error => {
        console.log(error)
        this.alertService.error('Tweet Id not exist!!', { keepAfterRouteChange: false });
      }
    )
  }
}
