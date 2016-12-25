import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router'
import {LoginService} from "./login.service";
import {CookieService} from 'angular2-cookie/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [LoginService, CookieService]
})
export class LoginComponent implements OnInit {
  constructor(private loginService: LoginService,
              private router: Router,
              private cookieService: CookieService) {
  }

  lgn: String;
  psw: String;

  ngOnInit() {
    this.redirectIfNeeded();
  }

  applyLogin(): void {
    var promise = this.loginService.login(this.lgn, this.psw);
    promise.subscribe(data => {
      this.loginService.authorise(this.cookieService.get("auth-token"))
        .subscribe(data => {
          console.log("logged in: ok")
          this.redirectIfNeeded();
        }, error => {
          console.log(JSON.stringify(error.json()));
        });
    }, error => {
      console.log(JSON.stringify(error.json()));
    });
  }

  redirectIfNeeded() {
    if (!this.loginService.getSessionStatus()) {
      this.router.navigateByUrl('dashboard');
    }
  }
}
