import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SwaggerState } from '../../store/swagger/swagger.state';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SwaggerService {
  baseUrl: string = environment.baseUrl + '/api/microservice';
  pageOptions='?pageSize=10&page=0';


  constructor(private store:Store<SwaggerState>, private http: HttpClient) { }

  getAllSwaggers(): Observable<any> {
    return this.http.get(this.baseUrl + '/all-swaggers' + this.pageOptions);
  }
}
