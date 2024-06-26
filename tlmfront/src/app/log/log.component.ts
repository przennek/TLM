import { Component, OnInit } from '@angular/core';
import {AppComponent} from "../app.component";
import {LogService} from "./log.service";
import {CookieService} from 'angular2-cookie/core';
import {LoginService} from "../login/login.service";
import {Router} from "@angular/router";
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'app-log',
  templateUrl: './log.component.html',
  styleUrls: ['./log.component.css'],
  providers: [LogService, CookieService, LoginService]
})
export class LogComponent implements OnInit {
  disabled: boolean;
  logs: String;
  level: String;
  phrase: String;

  constructor(
    private loginService: LoginService,
    private router: Router,
    private logService: LogService
  ) { }

  ngOnInit() {
    this.redirectIfNeeded();
    this.level = "FULLTEXT";
    this.logs = "Execute your query to get me started";
  }

  redirectIfNeeded() {
    if (this.loginService.getSessionStatus()) {
      this.router.navigateByUrl('login');
    }
  }

  search(): void {
    var promise;

    if(this.level === "FULLTEXT") {
      promise = this.logService.getLogsGivenPhrase(this.phrase)
    } else {
      promise = this.logService.getLogsGivenLevel(this.level)
    }

    promise.subscribe(data => {
      if(data.text() === "NOTHING FOUND") {
        this.logs = data.text();
      } else {
        let obj = JSON.parse(data.text());
        this.logs = "";

        for (var i = 0; i < obj.length; i++) {
          this.logs += obj[i]._source.date + "  "
            + obj[i]._source.level  + " --- " + "[" + obj[i]._source.className + "] " + obj[i]._source.message + "<br/>";
        }
      }
    }, error => {
      console.log(JSON.stringify(error.json()));
    });
  }

  select(): void {
    this.disabled = this.level !== "FULLTEXT";
  }
}
