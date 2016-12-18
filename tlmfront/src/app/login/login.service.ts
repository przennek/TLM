import { Injectable } from '@angular/core';
import {AppComponent} from "../app.component";

@Injectable()
export class LoginService {
  getSessionStatus(): boolean {
    console.log(AppComponent.token)
    return AppComponent.token === undefined ? false : true;
  }

  login(uname: String, passwd: String): void {
    AppComponent.token = "TOKEN"
  }
}
