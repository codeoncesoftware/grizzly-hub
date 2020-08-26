import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from 'src/app/auth/auth.service';
import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { AuthState } from 'src/app/store/authentication/auth.state';
import * as authActions from './../../store/authentication/auth.actions';

@Injectable()
export class GithubResolver implements Resolve<any> {
    constructor(private authService: AuthService, private router: Router, private store: Store<AuthState>) { }

    resolve(route: ActivatedRouteSnapshot): any {
        if (localStorage.getItem('token')) {
        console.log(route.queryParams.code);
        return this.authService.githubLogin(route.queryParams.code).subscribe(res => {
            if (res) {
                // tslint:disable-next-line: no-string-literal
                const email = res['email'];
                // tslint:disable-next-line: no-string-literal
                localStorage.setItem('token', res['access_token']);
                localStorage.setItem('userEmail', email);
                this.router.navigate(['/app/dashboard']);
                this.store.dispatch(new authActions.LoginUser(email));
            }
        }, (err) => {
            this.store.dispatch(new authActions.LoginGithubError(true));
            this.router.navigate(['/login']);
        });
    }
}
}
