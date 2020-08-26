import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  baseUrl: string = environment.baseUrl + '/api/notification';
  pageOptions='pageSize=10&page=0';

  constructor(private http: HttpClient) { }
  public getAllNotifications(): Observable<any> {
    if (localStorage.getItem('token')) {
      return this.http.get(this.baseUrl + '/findByEmail?email=' + localStorage.getItem('userEmail')+'&'+this.pageOptions);
    }
  }
  public allNotificationsSeen() {
    this.http.put(this.baseUrl + '/allSeen/' + localStorage.getItem('userEmail'), {}).subscribe(e => { });
  }
  public deleteAllNotifications() {
    this.http.delete(this.baseUrl + '/clearAll/' + localStorage.getItem('userEmail')).subscribe(e => { });
  }

  public getLatestNotifications(): Observable<any>{
    return this.http.get(this.baseUrl+ '/findByEmail?email=' + localStorage.getItem('userEmail')+'&'+this.pageOptions);
  }

  public deleteNotification(id){
    return this.http.delete(this.baseUrl + '/delete/' + id);
  }
}


