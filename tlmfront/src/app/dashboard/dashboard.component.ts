import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router'
import {LoginService} from "../login/login.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  providers: [LoginService]
})
export class DashboardComponent implements OnInit {
  constructor(
    private loginService: LoginService,
    private router: Router
  ) { }

  ngOnInit() {
    if (this.loginService.getSessionStatus() === false) {
      this.router.navigateByUrl('login');
    }
  }

}
