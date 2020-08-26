import { Component, OnInit } from '@angular/core';
import { APPCONFIG } from '../../config';
import { buildInfo} from '../../../generated/build-info';
import { Router } from '@angular/router';
import * as microservice from '../../store/microservice/microservice.actions';
import { Store } from '@ngrx/store';
import { MicroservicesState } from 'src/app/store/microservice/mircoservice.state';
@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent implements OnInit {

  AppConfig;
  buildInfo;
  anonymos = localStorage.getItem('token')


  constructor(public router : Router ,  public store: Store<MicroservicesState>, ) { }

  ngOnInit() {
    this.AppConfig = APPCONFIG;
    this.buildInfo = buildInfo;
  }
  load(){
    if (localStorage.getItem('token')) {
      this.router.navigateByUrl('/app/dashboard')
      this.store.dispatch(
        new microservice.LoadAllMicroservices({})
      );
    } else this.router.navigateByUrl('/app/dashboard')
  }
  toggleCollapsedNav() {
    this.AppConfig.navCollapsed = !this.AppConfig.navCollapsed;
  }

}
