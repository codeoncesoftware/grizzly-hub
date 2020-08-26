import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class MicroserviceService {

  baseUrl: string = environment.baseUrl + '/api/microservice';
  baseUrlSwagger: string = environment.baseUrl + '/api/swagger';
  pageOptions = '?pageSize=5&page=0';
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*'
    })
  };

  constructor(private http: HttpClient) { }

  public getMiroserviceByName(name): Observable<any> {
    return this.http.get(this.baseUrl + '/public/findByName/' + name);
  }

  public getAllMicroservices(pageSize, page): Observable<any> {
    const pageOptions = `?pageSize=${pageSize}&page=${page}`;
    return this.http.get(this.baseUrl + '/all' + pageOptions);
  }

  public getAllPublicMicroservices(pageSize,page): Observable<any> {
    const pageOptions = `?pageSize=${pageSize}&page=${page}`;
    return this.http.get(this.baseUrl + '/public/allPublic'+pageOptions);
  }

  public getMicroserviceById(id) {
    return this.http.get(this.baseUrl + '/find/' + id);
  }

  public addMicroserviceAttachFile(formData) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*'
      })
    };
    return this.http.post(this.baseUrl + '/add-swaggers', formData, httpOptions);
  }

  public updateMicroserviceAttachFile(formData) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*'
      })
    };
    return this.http.put(this.baseUrl + '/update-all-swaggers', formData, httpOptions);
  }

  public deleteMicroserviceById(id) {
    return this.http.delete(this.baseUrl + '/delete/' + id, this.httpOptions);
  }
  public checkMicroserviceNameUnicity(title) {
    return this.http.get(this.baseUrl + '/only/' + title)
  }
  // Swagger Endpoints
  public getSwaggers() {
    return this.http.get(this.baseUrl + '/all-swaggers' + this.pageOptions);
  }

  public getSwaggerDetails(microserviceId) {
    return this.http.get(this.baseUrl + '/' + microserviceId + '/swaggerDetails');
  }
  public getSwaggersByMicroserviceId(id) {
    return this.http.get(this.baseUrlSwagger + '/' + id + '/all-swaggers-ById');
  }

  public getSwaggerDetailsPublic(microserviceId) {
    return this.http.get(this.baseUrl + '/public/' + microserviceId + '/swaggerDetails');
  }
  public getSwaggersByMicroserviceIdPublic(id) {
    return this.http.get(this.baseUrlSwagger + '/public/' + id + '/all-swaggers-ById');
  }

  public getContent(id) {
    console.log(this.baseUrl + '/getContent/' + id)
    return this.http.get(this.baseUrl + '/getContent/' + id);
  }

  public checkSwaggerURL(swagger) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Access-Control-Allow-Origin': '*'
      })
    };
    return this.http.post(this.baseUrl + '/checkSwagger', swagger, httpOptions);
  }
  public getSwggersByMicroserviceIdFiltredByEnv(id) {
    return this.http.get(this.baseUrlSwagger + '/' + id + '/swaggersByEnv');
  }
  public getSwggersByMicroserviceIdFiltredByVer(id) {
    return this.http.get(this.baseUrlSwagger + '/' + id + '/swaggers');
  }
  public getSwaggersVersionByMicroserviceIdAndEnv(id, env) {
    return this.http.get(this.baseUrlSwagger + '/' + id + '/' + env + '/swaggersVersions');
  }

  public importSwaggerOnExistingContainer(file: File): Observable<string> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.http.post<string>(this.baseUrl + '/api/container/importSwaggerOnExistingContainer', formData);
  }

}
