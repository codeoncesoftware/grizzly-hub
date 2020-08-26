import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router, ActivatedRoute, Data } from '@angular/router';
import * as authActions from './../../store/authentication/auth.actions';
import { catchError } from 'rxjs/operators';
import { AppTranslateService } from 'src/app/shared/services/app-translate-service';
import { Store } from '@ngrx/store';
import { AuthState } from 'src/app/store/authentication/auth.state';

@Component({
  selector: 'app-login',
  styleUrls: ['./login.component.scss'],
  templateUrl: './login.component.html'
})

export class LoginComponent implements OnInit {
  userObj = { email: '', password: '' };
  token: string;
  errorMessage: string;
  email = '';
  pass = '';
  logged = true;
  error: boolean;
  // Reset Password Section
  emailToSendResetPass: string;
  resetPasswordBool = false;
  successfulReset = false;
  resetErrorMessage = false;
  githubLoginError = false;

  isFr: boolean;
  selectedLanguage: string;

  constructor(private authService: AuthService, private store: Store<AuthState>, private router: Router, private appTranslateService: AppTranslateService) {
  }

  ngOnInit() {
    if(localStorage.getItem('token')){
    this.store.select('auth').subscribe(res => {
      console.log(res);
      if (res.githubError) {
        this.githubLoginError = true;
      }
    });
  }
    // Set i18n Language
    if (localStorage.getItem('grizzly-lang')) {
      this.setLang(localStorage.getItem('grizzly-lang').toLowerCase());
    } else {
      this.setLang(navigator.language);
    }
  }

  setLang(lang: string) {

    this.appTranslateService.setDefaultLang(lang);
    localStorage.setItem('grizzly-lang', lang.toLowerCase());

    if (lang.includes('fr')) {
      this.selectedLanguage = 'FR';
      this.isFr = false;
    } else {
      this.selectedLanguage = 'EN';
      this.isFr = true;
    }

  }

  login() {
    localStorage.clear();
    this.userObj.email = this.email;
    this.userObj.password = this.pass;
    this.error = false;
    if (this.email && this.pass) {
      this.authService.login(this.userObj).subscribe(res => {
        if (res) {
          this.logged = true;
          // Save selected language
          localStorage.setItem('grizzly-lang', this.selectedLanguage.toLowerCase());
          // tslint:disable-next-line: no-string-literal
          localStorage.setItem('token', res['access_token']);
          console.log(res);
          localStorage.setItem('userEmail', this.email);
          this.router.navigate(['/app/dashboard']);
          this.store.dispatch(new authActions.LoginUser(this.userObj.email));
        }
      },
        err => {
          if (err.status === 401) {
            if (err.error.error_description === '4011') {
              this.errorMessage = 'auth.signin.errors.credentials';
            } else if (err.error.error_description === '4014') {
              this.errorMessage = 'Github email exists';
            } else {
              this.errorMessage = 'auth.signin.errors.validAccount';
            }
            this.logged = false;
          }
        }
      );
    } else {
      this.error = true;
    }

  }

  githubLogin() {
    this.authService.redirectGithubLoginPage();
  }

  showResetPassword() {
    this.resetPasswordBool = !this.resetPasswordBool;
    this.successfulReset = false;
  }

  resetPassword() {
    const lang = localStorage.getItem('grizzly-lang');

    if (this.emailToSendResetPass) {
      this.authService.sendResetEmail(this.emailToSendResetPass, lang).subscribe(() => {
        this.successfulReset = true;
        this.resetErrorMessage = false;
      },
        err => {
          if (err.status === 401) {
            if (err.error.error_description === '4013') {
              this.successfulReset = false;
              this.resetErrorMessage = true;
            }
          }
        }
      );
    }
  }


}
