import {Injectable, Inject} from '@angular/core';
import {AppComponent} from "../app.component";
import {Headers, Http, Response} from "@angular/http";
import {CookieService} from 'angular2-cookie/core';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class LoginService {
  private baseUrl: String  = "..";
  constructor(
    private http: Http,
    @Inject(CookieService) private cookieService: CookieService
  ) {
    this.http = http;
  }

  getSessionStatus(): boolean {
    return this.cookieService.get("auth-token") === undefined
  }

  login(uname: String, passwd: String) {
    var body = 'login='+ uname+ '&' + 'password=' + passwd;
    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    return this.http
      .post(this.baseUrl+'/auth/login',
        body, {
          headers: headers,
          withCredentials: true
        });
  }

  authorise(token: string) {
    var body = '';
    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    headers.append('auth-token', token);

    return this.http
      .post(this.baseUrl+'/auth/login-success',
        body, {
          headers: headers,
          withCredentials: true
        });
  }
}
