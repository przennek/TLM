import {Component, OnInit, ChangeDetectorRef} from "@angular/core";
import {Router} from "@angular/router";
import {LoginService} from "../login/login.service";
import {CookieService} from "angular2-cookie/core";
import {DashboardService} from "./dashboard.service";
import { Observable } from 'rxjs/Observable';
import 'rxjs/Rx';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  providers: [LoginService, CookieService, DashboardService]
})
export class DashboardComponent implements OnInit {
  constructor(private loginService: LoginService,
              private dashboardService: DashboardService,
              private ref: ChangeDetectorRef,
              private router: Router) {
  }

  moduleList = [];
  selectedModule: string = null;

  ngOnInit() {
    if (this.loginService.getSessionStatus()) {
      this.router.navigateByUrl('login');
    }
    this.dashboardService.getModules()
      .subscribe(data => {
        this.moduleList = JSON.parse(JSON.parse(data.text()));
      }, error => {
        console.log(JSON.stringify(error.json()));
      });
  }

  onClick(module: string) {
    this.selectedModule = module;
    this.ref.detectChanges()
  }
}
