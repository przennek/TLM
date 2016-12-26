import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router'
import {LoginService} from "../login/login.service";
import {CookieService} from 'angular2-cookie/core';
import {DashboardService} from "./dashboard.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  providers: [LoginService, CookieService, DashboardService]
})
export class DashboardComponent implements OnInit {
  constructor(
    private loginService: LoginService,
    private dashboardService: DashboardService,
    private router: Router
  ) { }

  moduleList = [];
  selectedModule: string = null;

  ngOnInit() {
    if (this.loginService.getSessionStatus()) {
      this.router.navigateByUrl('login');
    }
    this.moduleList = this.dashboardService.getModules();
  }

  onClick(module: string) {
    this.selectedModule = module;
  }
}
