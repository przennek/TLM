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

  lgn: string;
  psw: string;
  validatePsw: string;
  email: string;

  message: string;
  shouldMessageBeVisible: boolean;

  register: boolean;

  ngOnInit() {
    this.redirectIfNeeded();
    this.register = false;
    this.shouldMessageBeVisible = false;
    this.message = "Additional action is required to register";
  }

  applyLogin(): void {
    var promise = this.loginService.login(this.lgn, this.psw);
    promise.subscribe(data => {
      this.loginService.authorise(this.cookieService.get("auth-token"))
        .subscribe(data => {
          console.log("logged in: ok");
          this.cookieService.put("login", this.lgn);
          this.redirectIfNeeded();
        }, error => {
          console.log(JSON.stringify(error.json()));
        });
    }, error => {
      this.shouldMessageBeVisible = true;
      this.message = "Wrong login or password.";
    });
  }

  redirectIfNeeded() {
    if (!this.loginService.getSessionStatus()) {
      this.router.navigateByUrl('dashboard');
    }
  }

  applyRegister() {
    if (!this.register) {
      this.shouldMessageBeVisible = true;
      this.register = true;
    } else {
      this.shouldMessageBeVisible = true;
      this.message = "Your request is pending for approval.";
    }
  }
}
