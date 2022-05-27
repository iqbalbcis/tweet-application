import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { LoginComponent } from './component/login/login.component';
import { MenuandheaderComponent } from './component/menuandheader/menuandheader.component';
import { FooterComponent } from './component/footer/footer.component';
import { DashboardComponent } from './component/dashboard/dashboard.component';
import { ErrorComponent } from './component/error/error.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HTTPINTERCEPTORScepterAuthServiceService } from 'src/app/service/interceptor/jwt-interceptorscepter-auth-service.service';
import { SpinnerComponent } from './component/spinner/spinner.component';
import { SpinnerInterceptorService } from './service/interceptor/spinner-interceptor.service';
import { AlertComponent } from './component/alert/alert.component';
import { SuccessMessageComponent } from './component/success-message/success-message.component';
import { CommonModule} from '@angular/common';
import { NgxPrintModule } from 'ngx-print';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegisterComponent } from './component/user-component/register/register.component';
import { AllUsersComponent } from './component/user-component/all-users-find/all-users.component';
import { SearchSpecificUserComponent } from './component/user-component/find-specific-user/search-specific-user/search-specific-user.component';
import { LoadSpecificUserComponent } from './component/user-component/find-specific-user/load-specific-user/load-specific-user.component';
import { PostTweetComponent } from './component/tweet-component/post-tweet/post-tweet.component';
import { ResetPasswordComponent } from './component/user-component/reset-password/reset-password.component';
import { ForgotPasswordComponent } from './component/user-component/forgot-password/forgot-password.component';
import { DeleteTweetComponent } from './component/tweet-component/delete-tweet/delete-tweet.component';
import { ReplyModalComponent } from './component/tweet-component/modals/reply-modal/reply-modal.component';
import {BsModalService} from "ngx-bootstrap/modal";
import { MessageComponent } from './component/message/message.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    MenuandheaderComponent,
    FooterComponent,
    DashboardComponent,
    ErrorComponent,
    SuccessMessageComponent,
    SpinnerComponent,
    AlertComponent,
    RegisterComponent,
    AllUsersComponent,
    SearchSpecificUserComponent,
    LoadSpecificUserComponent,
    PostTweetComponent,
    ResetPasswordComponent,
    ForgotPasswordComponent,
    DeleteTweetComponent,
    ReplyModalComponent,
    MessageComponent
  ],
  imports: [
    BrowserModule,
    // import HttpClientModule after BrowserModule.
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    CommonModule,
    NgxPrintModule,
    BsDatepickerModule.forRoot(),
    BrowserAnimationsModule
  ],
  providers: [BsModalService, {provide: HTTP_INTERCEPTORS, useClass: HTTPINTERCEPTORScepterAuthServiceService , multi: true },
              {provide: HTTP_INTERCEPTORS, useClass: SpinnerInterceptorService , multi: true }],

  bootstrap: [AppComponent]
})
export class AppModule { }
