import {Injectable, Inject} from '@angular/core';
import {AppComponent} from "../app.component";
import {Headers, Http, Response} from "@angular/http";
import {CookieService} from 'angular2-cookie/core';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class TestDetailService {
  private baseUrl: String  = "..";
  constructor(
    private http: Http,
    @Inject(CookieService) private cookieService: CookieService
  ) {
    this.http = http;
  }

  getTestDetail(token: string) {
      var body = 'tokenId=' + token;
      var headers = new Headers();
      headers.append('Content-Type', 'application/x-www-form-urlencoded');
      headers.append('auth-token', this.cookieService.get("auth-token"));

      return this.http
        .post(this.baseUrl+'/frontendservice/getTest',
          body, {
            headers: headers,
            withCredentials: true
          });
  }
}
