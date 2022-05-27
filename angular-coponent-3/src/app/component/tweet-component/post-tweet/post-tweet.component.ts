import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AlertService } from 'src/app/service/alert/alert.service';
import { AuthenticationServiceService } from 'src/app/service/authentication/authentication-service.service';
import { GlobalService } from 'src/app/service/global/global.service';
import { TweetService } from 'src/app/service/tweet/tweet.service';
import { BsModalRef, BsModalService } from "ngx-bootstrap/modal";

@Component({
  selector: 'app-post-tweet',
  templateUrl: './post-tweet.component.html',
  styleUrls: ['./post-tweet.component.css']
})
export class PostTweetComponent implements OnInit {

  form: FormGroup;
  form1: FormGroup;
  form2: FormGroup;

  submitted: boolean = false;
  submitted1: boolean = false;
  submitted2: boolean = false;

  @ViewChild('tweetReply') tweetReplyModal: TemplateRef<any>;
  @ViewChild('tweetEdit') tweetEditModal: TemplateRef<any>;

  postListArr: Array<any> = [];

  totalCount = '';
  checkListData:boolean = false;

  modalRef: BsModalRef;
  selObj: any = {};

  constructor(
    private formBuilder: FormBuilder,
    private modalService: BsModalService,
    private authenticationService: AuthenticationServiceService,
    private tweetService: TweetService,
    private alertService: AlertService) {
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({

      username: ['', Validators.required],
      text: ['', Validators.required]
    })

    this.form1 = this.formBuilder.group({
      text: ['', Validators.required]
    })

    // reply form
    this.form2 = this.formBuilder.group({
      text: ['', Validators.required]
    })

    this.form.controls.username.setValue(this.authenticationService.getAuthenticatedUser());
    this.findAllTweetAndReply();
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.form.controls;
  }

  get f1() {
    return this.form1.controls;
  }

  get f2() {
    return this.form2.controls;
  }

  postNewTweet() {
    this.submitted = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.form.invalid) {
      //this.globalService.goToTop();
      return;
    }

    this.tweetService.addTweetDetails(this.form.value).subscribe(
      data => {
        console.log(data)
        this.submitted = false;
        this.f.text.setValue('');
        this.findAllTweetAndReply();
      },
      error => {
        console.log(error);
        this.alertService.error(error.error.message + '!!!', { keepAfterRouteChange: false });
      })
  }

  //edit and update
  updateTweet(tweetId: number) {
    this.submitted1 = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.form1.invalid) {
      return;
    }

    this.tweetService.updateTweetDetails(tweetId, this.form1.value).subscribe(
      data => {
        console.log(data)
        this.submitted1 = false;
        this.f1.text.setValue('');
        this.modalRef.hide();
        this.findAllTweetAndReply();
      },
      error => {
        console.log(error);
      })
  }

  // update like //doesn't have interact with body
  updateTweetLike(tweetId: number) {

    this.alertService.clear();

    this.tweetService.updateTweetLike(this.getUserName(), tweetId, this.form.value).subscribe(
      data => {
        console.log(data)
        this.findAllTweetAndReply();
      },
      error => {
        console.log(error);
      }
    )
  }

  findAllTweetAndReply() {

    // reset alerts on submit
    this.alertService.clear();

    this.tweetService.findAllTweetAndReply(this.getUserName()).subscribe(
      data => {
        console.log(data.tweetList);
        this.postListArr = data.tweetList;
      },
      error => {
        console.log(error);
      }
    )
  }

  deleteTweetDetails(tweetId: number) {

    this.tweetService.deleteTweetDetails(tweetId).subscribe(
      data => {
        console.log(data);
        this.findAllTweetAndReply();
      },
      error => {
        console.log(error);
      }
    )
  }

  addTweetReply(tweetId: number) {
    this.submitted2 = true;

    // reset alerts on submit
    this.alertService.clear();

    // stop here if form is invalid
    if (this.form2.invalid) {
      return;
    }

    this.tweetService.addReplyDetails(this.getUserName(), tweetId, this.form2.value).subscribe(
      data => {
        console.log(data);
        this.closeModalRefForReply();
       // this.getAllTweetsInArray();
        this.findAllTweetAndReply();
      },
      error => {
        console.log(error);
      })
  }

  totalLikeCount(tweetId: number) {

    this.tweetService.totalLikeCount(tweetId).subscribe(
      data => {
        console.log(data);
        if (data.count > 0) {
          this.totalCount = '' + data.count;
        }
      },
      error => {
        console.log(error);
      }
    )
  }

  getUserName() {
    return this.authenticationService.getAuthenticatedUser();
  }

  closeModalRefForReply() {
    this.submitted2 = false;
    this.f2.text.setValue('');
    this.modalRef.hide();
  }

  editModal(obj) {
    console.log(obj)
    this.selObj = obj;
    this.form1.controls.text.setValue(this.selObj.text);
    this.modalRef = this.modalService.show(this.tweetEditModal);
  }

  replyModal(obj) {
    this.selObj = obj;
    this.modalRef = this.modalService.show(this.tweetReplyModal);
  }
}
