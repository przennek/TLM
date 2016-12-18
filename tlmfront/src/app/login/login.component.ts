import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router'
import {LoginService} from "./login.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [LoginService]
})
export class LoginComponent implements OnInit {
  constructor(
    private loginService: LoginService,
    private router: Router
  ) { }

  lgn: String;
  psw: String;

  ngOnInit() {
    this.redirectIfNeeded();
  }

  applyLogin(): void {
    this.loginService.login(this.lgn, this.psw);
    this.redirectIfNeeded();
  }

  redirectIfNeeded() {
    if (this.loginService.getSessionStatus() === true) {
      this.router.navigateByUrl('dashboard');
    }
  }
}
