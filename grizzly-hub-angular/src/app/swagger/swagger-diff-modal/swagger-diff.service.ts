import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SwaggerDiffService {

  baseUrl: string = environment.baseUrl + '/api/microservice';
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*'
    })
  };
  constructor(private http: HttpClient) { }

  getSwaggerDiifs(Ids: any): Observable<any> {
    return this.http.post(this.baseUrl + '/swagger-diff',
      JSON.stringify(Ids),
      this.httpOptions
    )
  }  getSwaggerCompare(Ids: any): Observable<any> {
    return this.http.post(this.baseUrl + '/compare-diff',
      JSON.stringify(Ids),
      this.httpOptions
    )
  }
}
