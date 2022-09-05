import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-success-message',
  templateUrl: './success-message.component.html',
  styleUrls: ['./success-message.component.css']
})
export class SuccessMessageComponent implements OnInit {

  concatedMessage:string = '';
  fileRef:string = '';

  constructor(
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.fileRef = this.route.snapshot.params['fileRefNumber'];
    this. concatMessage();
  }

  concatMessage()
  {
    if(this.fileRef === undefined) {
      this.concatedMessage = 'Your submission has been successful!!!';
    }
    else if(this.fileRef === 'user')
    {
      this.concatedMessage = 'Your submission has been successful and admin needs to activate this file!!!';
    }
    else {
      this.concatedMessage = 'Your submission has been successful and file reference number: '+this.fileRef;
    }
  }

}
