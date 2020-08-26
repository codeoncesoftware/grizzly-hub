import { Injectable } from '@angular/core';

// tslint:disable-next-line:prefer-const
import SockJs from 'sockjs-client';
import Stomp from 'stompjs';
import { environment } from '../../environments/environment';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})



@Injectable()
export class WebSocketService {
  baseUrl: string = environment.baseUrl;

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*'
    })
  }

  // Open connection with the back-end socket
  public connect() {
    const socket = new SockJs(this.baseUrl + '/socket');

    const stompClient = Stomp.over(socket);

    return stompClient;
  }
}
