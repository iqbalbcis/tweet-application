import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http:HttpClient) { }

  registerUserDetails(user: object): Observable<any>
  {
    return this.http.post(
      `${API_URL}/tweets/register`
        , user);
  }

  findAllUsers(): Observable<any>
  {
    return this.http.get(
      `${API_URL}/tweets/users/all`);
  }

  findUserUsingUserName(username:string): Observable<any>
  {
    return this.http.get(
      `${API_URL}/tweets/user/search/${username}`);
  }

  findAUserUsingEmailForgotPassword(userEmail:string): Observable<any>
  {
    return this.http.get(
      `${API_URL}/tweets/forgot/${userEmail}`);
  }

  resetPassword(username:string, user:string): Observable<any>
  {
    return this.http.put(
      `${API_URL}/tweets/resetPassword/${username}`
        , user);
  }

}
