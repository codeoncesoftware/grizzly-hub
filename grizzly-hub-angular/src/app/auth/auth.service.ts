import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from '../shared/models/User';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  baseUrl: string = environment.baseUrl;

  constructor(private http: HttpClient, private router: Router, private translateService: TranslateService) { }

  login(userObj) {
    return this.http.post<string>(this.baseUrl + '/api/auth/login', userObj, { responseType: 'json' });
  }

  logout() {
    this.http.get<boolean>(this.baseUrl + '/api/user/logout').subscribe(res => {
      const lang = localStorage.getItem('grizzly-lang');
      localStorage.clear();
      // Keep selected lang
      if (lang) {
        localStorage.setItem('grizzly-lang', lang);
      }
      this.router.navigate(['/login']);
    });
  }

  redirectGithubLoginPage() {
    window.location.href = environment.GITHUB_AUTHORIZE_URL + '?client_id=' + environment.GITHUB_CLIENT_ID + '&reditect_uri=' + environment.GITHUB_REDIRECT_URI;
  }

  githubLogin(code) {
    return this.http.get(this.baseUrl + '/api/auth/github/login', { params: { code } });
  }

  signup(userObj) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept-Language': this.translateService.getDefaultLang()
    });
    return this.http.post<string>(this.baseUrl + '/api/auth/signup', userObj, { headers });
  }

  check(email: string) {
    return this.http.get<boolean>(this.baseUrl + '/api/auth/check/' + email);
  }

  sendResetEmail(email: string, lang: string) {
    const headers = new HttpHeaders({
      'Accept-Language': lang
    });
    return this.http.get(this.baseUrl + '/api/user/send/reset/password/' + email, { headers });
  }

  resetPassword(token, password) {
    return this.http.post(this.baseUrl + '/api/user/reset/password/' + token, { password });
  }

  confirmEmail(token) {
    return this.http.get(this.baseUrl + '/api/auth/confirm/email/' + token);
  }

  getUser(email): Observable<any> {
    if (localStorage.getItem('token')) {
      return this.http.get<any>(this.baseUrl + '/api/user/' + email);
    }
  }

  updateProfile(user): Observable<any> {
    return this.http.put<any>(this.baseUrl + '/api/user/update/user', user);
  }

  updatePassword(oldPwd, newPwd) {
    return this.http.put(this.baseUrl + '/api/user/update/pwd', {}, { params: { oldPwd, newPwd } });
  }

  checkToken(token): Observable<boolean> {
    return this.http.get<boolean>(this.baseUrl + '/api/auth/check/token/' + token);
  }
  getTeams(){
    return this.http.get(this.baseUrl + '/api/user/teams');
  }
  addTeam(team){
    return this.http.post(this.baseUrl + '/api/user/teams' , team);
  }
  getUsersByTeam(){
    return this.http.get(this.baseUrl + '/api/user/teammates');
  }
  getSubsribedUsersByMicroserviceId(id){
    return this.http.get(this.baseUrl + '/api/subscription/findByMicroserviceId/' + id);
  }

  getAllUsers() {
    return this.http.get<User[]>(this.baseUrl + '/api/user/all');
  }

  sendInvitation(userEmails, lang, orgId, orgName) {
    const headers = new HttpHeaders({
      'Accept-Language': lang
    });
    return this.http.post<any>(this.baseUrl + '/api/user/invite', userEmails, {headers , params: { orgId, orgName } });
  }
}
