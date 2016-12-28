import {Injectable, Inject} from '@angular/core';
import {AppComponent} from "../app.component";
import {Headers, Http, Response} from "@angular/http";
import {CookieService} from 'angular2-cookie/core';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class TestDetailService {
  constructor(
    private http: Http,
    @Inject(CookieService) private cookieService: CookieService
  ) {
    this.http = http;
  }

  getTestDetail(token: string) {
    return JSON.parse('{"tokenId":"08840ec2-2a8e-4940-9c61-9abace3e4221","testType":"UnitTest","className":"pl.edu.agh.util.TestClassDataExtractorTest","classComment":"Test comment to test class","classTags":[{"tagName":"@author","tagText":"Test author tag"}],"testMethods":[{"methodName":"validateTestClassName()","methodComment":"","methodTags":[],"methodParameters":[]},{"methodName":"validateTestClassTags()","methodComment":"","methodTags":[],"methodParameters":[]},{"methodName":"validateTestClassDescription()","methodComment":"","methodTags":[],"methodParameters":[]}]}');
  }

  // authorise(token: string) {
  //   var body = '';
  //   var headers = new Headers();
  //   headers.append('Content-Type', 'application/x-www-form-urlencoded');
  //   headers.append('auth-token', token);
  //
  //   return this.http
  //     .post('http://localhost:8080/auth/login-success',
  //       body, {
  //         headers: headers,
  //         withCredentials: true
  //       });
  // }
}
