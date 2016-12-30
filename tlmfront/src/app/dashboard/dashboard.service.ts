import {Injectable, Inject} from "@angular/core";
import {Headers, Http, Response} from "@angular/http";
import {CookieService} from "angular2-cookie/core";
import { Observable } from 'rxjs/Observable';

@Injectable()
export class DashboardService {
  constructor(
    private http: Http,
  @Inject(CookieService) private cookieService: CookieService
  ) {
    this.http = http;
  }

  getModules() {
    var body = '';
    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    headers.append('auth-token', this.cookieService.get("auth-token"));
    return this.http
      .post('http://localhost:8080/frontendservice/getAllTestTrees',
        body, {
          headers: headers
        });
  }
}
