import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router'
import {LoginService} from "../login/login.service";
import {CookieService} from 'angular2-cookie/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  providers: [LoginService, CookieService]
})
export class DashboardComponent implements OnInit {
  constructor(
    private loginService: LoginService,
    private router: Router
  ) { }

  ngOnInit() {
    if (this.loginService.getSessionStatus()) {
      this.router.navigateByUrl('login');
    }
  }

}
