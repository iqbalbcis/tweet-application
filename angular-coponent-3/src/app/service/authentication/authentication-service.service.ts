import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL, AUTHENTICATED_USER, TOKEN } from 'src/app/app.constants';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationServiceService {

  constructor(private http: HttpClient) { }

  loginService(username, passWord): Observable<any> {

    return this.http.post<any>(
      `${API_URL}/api/v1.0/tweets/login`, {
      username,
      passWord
    }).pipe(
      map(
        data => {
          sessionStorage.setItem(AUTHENTICATED_USER, username);
          sessionStorage.setItem(TOKEN, `Bearer ${data.token}`);
          // console.log(data);
          localStorage.setItem(AUTHENTICATED_USER, username);
          return data;
        }
      )
    );
  }

  logoutService()
  {
    sessionStorage.removeItem(AUTHENTICATED_USER);
    sessionStorage.removeItem(TOKEN);
    localStorage.removeItem(AUTHENTICATED_USER);
  }

  getAuthenticatedUser() {
    return sessionStorage.getItem(AUTHENTICATED_USER)
  }

  getAuthenticatedToken() {
    if (this.getAuthenticatedUser())
      return sessionStorage.getItem(TOKEN)
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem(AUTHENTICATED_USER)
    return !(user === null)
  }

  isUserAnAdmin()
  {
    let jwt = sessionStorage.getItem(TOKEN);
    let jwtData = jwt.split('.')[1]
    let decodedJwtJsonData = window.atob(jwtData)
    let decodedJwtData = JSON.parse(decodedJwtJsonData)

    return  decodedJwtData.isAdmin ? true : false;
  }

  refreshToken(): Observable<any> {
    return this.http.get(
      `${API_URL}/api/v1.0/tweets/refresh`);
  }

  resetToken(): Observable<any> {
    return this.http.get(
      `${API_URL}/api/v1.0/tweets/resetToken`);
  }
}

export class AuthenticationBean {
  constructor(public message: string) { }
}
