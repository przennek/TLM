import {Injectable, Inject} from "@angular/core";
import {Headers, Http} from "@angular/http";
import {CookieService} from "angular2-cookie/core";
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs';


@Injectable()
export class NavbarService {
  private baseUrl: String  = "..";
  constructor(
    private http: Http,
    @Inject(CookieService) private cookieService: CookieService
  ) {
    this.http = http;
  }

  propagateLogout() {
    var body = '';
    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    headers.append('auth-token', this.cookieService.get("auth-token"));
    return this.http
      .post(this.baseUrl+'/auth/logout?ref=http%3A%2F%2Ftlm.apricode.pl%2Fauth',
        body, {
          headers: headers
        }).subscribe(data => {
          console.log("logged out: ok")
        }, error => {
          console.log(JSON.stringify(error.json()));
        });
  }
}
