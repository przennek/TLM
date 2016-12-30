import { Component, OnInit } from '@angular/core';
import {CookieService} from 'angular2-cookie/core';
import {NavbarService} from "./navbar.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  providers: [CookieService, NavbarService]
})
export class NavbarComponent implements OnInit {
  page: string;

  constructor(
    private navbarService: NavbarService,
    private cookieService: CookieService,
  ) {}

  ngOnInit() {
    if(window.location.pathname === "/") {
      this.page = "/dashboard"
    } else {
      this.page = window.location.pathname;
    }
  }

  logout() {
    this.navbarService.propagateLogout();
    this.cookieService.remove("auth-token");
    this.cookieService.remove("login");
  }

  isLoggedIn() {
    return this.cookieService.get("auth-token") === undefined;
  }
}
