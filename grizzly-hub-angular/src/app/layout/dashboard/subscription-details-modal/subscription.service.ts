import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {

  private baseUrl = environment.baseUrl + '/api/subscription';
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*'
    })
  };
  constructor(private http: HttpClient) { }

  public getSubscriptions() {
    if (localStorage.getItem('anonymos')!=='true') {
    return this.http.get(this.baseUrl + '/allbyuser');
    }
  }
  public sendSubscription(subscription) {
    return this.http.post(this.baseUrl + '/subscribe', subscription);
  }

  public deleteSubscription(id) {
    return this.http.delete(this.baseUrl + '/delete/' + id);
  }

  public getSubscription(id) {
    return this.http.get<any>(this.baseUrl + '/findbyuser/' + id);
  }
  public isSubscribed(userEmail , microserviceId) {
    return this.http.get<any>(this.baseUrl + '/isSubscribed/' + userEmail + '/' + microserviceId);
  }
  public deleteMembersSubscription(userEmail , microserviceId){
    return this.http.delete(this.baseUrl + '/delete/' + userEmail + '/' + microserviceId , this.httpOptions);
  }
}


