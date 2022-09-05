import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './component/login/login.component';
import { DashboardComponent } from './component/dashboard/dashboard.component';
import { ErrorComponent } from './component/error/error.component';
import { RoutegauardServiceService } from './service/routeguard/routegauard-service.service';
import { SuccessMessageComponent } from './component/success-message/success-message.component';
import { RegisterComponent } from './component/user-component/register/register.component';
import { AllUsersComponent } from './component/user-component/all-users-find/all-users.component';
import { SearchSpecificUserComponent } from './component/user-component/find-specific-user/search-specific-user/search-specific-user.component';
import { LoadSpecificUserComponent } from './component/user-component/find-specific-user/load-specific-user/load-specific-user.component';
import { PostTweetComponent } from './component/tweet-component/post-tweet/post-tweet.component';
import { ResetPasswordComponent } from './component/user-component/reset-password/reset-password.component';
import { ForgotPasswordComponent } from './component/user-component/forgot-password/forgot-password.component';
import { DeleteTweetComponent } from './component/tweet-component/delete-tweet/delete-tweet.component';
import { MessageComponent } from './component/message/message.component';

// need import AppRoutingModule in app.module.ts and need to in app.component.ts -> <router-outlet></router-outlet>
const routes: Routes = [
  //{path:'', component: LoginComponent },
  {path:'', redirectTo: '/login', pathMatch: 'full' },
  {path:'login', component: LoginComponent },
  {path:'dashboard', component: DashboardComponent, canActivate: [RoutegauardServiceService] },
  {path:'successmessage', component: SuccessMessageComponent},
  {path:'successmessage/:fileRefNumber', component: SuccessMessageComponent, canActivate: [RoutegauardServiceService] },
  {path:'register', component: RegisterComponent },
  {path:'findAllUsers', component: AllUsersComponent, canActivate: [RoutegauardServiceService] },
  {path:'searchSpecificUser', component: SearchSpecificUserComponent, canActivate: [RoutegauardServiceService] },
  {path:'loadSpecificUser/:username', component: LoadSpecificUserComponent, canActivate: [RoutegauardServiceService] },
  {path:'reset-password/:username', component: ResetPasswordComponent },
  {path:'forgot-password', component: ForgotPasswordComponent },
  {path:'post-new-tweet', component: PostTweetComponent, canActivate: [RoutegauardServiceService] },
  {path:'delete-tweet', component: DeleteTweetComponent, canActivate: [RoutegauardServiceService] },
  {path:'message', component: MessageComponent, canActivate: [RoutegauardServiceService] },
  {path:'**', component: ErrorComponent, canActivate: [RoutegauardServiceService] }
];

// otherwise redirect to home
// { path: '**', redirectTo: '' }

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
