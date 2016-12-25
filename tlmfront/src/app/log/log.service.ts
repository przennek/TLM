import {Injectable} from "@angular/core";
import {Headers, Http} from "@angular/http";

@Injectable()
export class LogService {
  constructor(private http: Http) {
    this.http = http;
  }

  getLogsGivenPhrase(msg: String) {
    var body = 'msg=' + msg;
    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    return this.http
      .post('http://localhost:8080/logservice/logs/getContainingMessage',
        body, {
          headers: headers
        });
  }

  getLogsGivenLevel(level: String) {
    var body = 'level=' + level;
    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');
    return this.http.post('http://localhost:8080/logservice/logs/getAllByLevel',
        body, {
          headers: headers
        });
  }
}
