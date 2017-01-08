import {Injectable, Inject} from "@angular/core";
import {Headers, Http, Response} from "@angular/http";
import {CookieService} from "angular2-cookie/core";
import { Observable } from 'rxjs/Observable';

@Injectable()
export class ModuledetailService {
  private baseUrl: String  = "..";
  constructor(
    private http: Http,
    @Inject(CookieService) private cookieService: CookieService
  ) {
    this.http = http;
  }

  getModuleTree(module: string) {
      var body = 'moduleName=' + module;
      var headers = new Headers();
      headers.append('Content-Type', 'application/x-www-form-urlencoded');
      headers.append('auth-token', this.cookieService.get("auth-token"));
      return this.http
        .post(this.baseUrl+'/frontendservice/getTestTree',
          body, {
            headers: headers
          });
  }
}
