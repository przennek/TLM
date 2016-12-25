import { Component, OnInit } from '@angular/core';
import {AppComponent} from "../app.component";
import {LogService} from "./log.service";

@Component({
  selector: 'app-log',
  templateUrl: './log.component.html',
  styleUrls: ['./log.component.css'],
  providers: [LogService]
})
export class LogComponent implements OnInit {
  disabled: boolean;
  logs: String;
  level: String;
  phrase: String;

  constructor(
    private logService: LogService,
  ) { }

  ngOnInit() {
    this.level = "FULLTEXT";
    this.logs = "Execute your query to get me started";
  }

  search(): void {
    var promise;

    if(this.level === "FULLTEXT") {
      promise = this.logService.getLogsGivenPhrase(this.phrase)
    } else {
      promise = this.logService.getLogsGivenLevel(this.level)
    }

    promise.subscribe(data => {
      this.logs = data.text();
    }, error => {
      console.log(JSON.stringify(error.json()));
    });
  }

  select(): void {
    this.disabled = this.level !== "FULLTEXT";
  }
}
