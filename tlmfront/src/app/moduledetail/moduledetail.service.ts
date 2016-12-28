import {Injectable, Inject} from "@angular/core";
import {Headers, Http} from "@angular/http";
import {CookieService} from "angular2-cookie/core";

@Injectable()
export class ModuledetailService {
  constructor(
    private http: Http,
  @Inject(CookieService) private cookieService: CookieService
  ) {
    this.http = http;
  }

  getModuleTree(module: string) {
    return [{"name":"java","children":[{"name":"pl","children":[{"name":"edu","children":[{"name":"agh","children":[{"name":"util","children":[{"name":"FileHelperTest.java","token":""},{"name":"TestClassDataExtractorTest.java","token":""}]}]}]}]}]}]
  }

  // getLogsGivenPhrase(msg: String) {
  //   var body = 'msg=' + msg;
  //   var headers = new Headers();
  //   headers.append('Content-Type', 'application/x-www-form-urlencoded');
  //   headers.append('auth-token', this.cookieService.get("auth-token"));
  //   return this.http
  //     .post('http://localhost:8080/logservice/logs/getContainingMessage',
  //       body, {
  //         headers: headers
  //       });
  // }
}
