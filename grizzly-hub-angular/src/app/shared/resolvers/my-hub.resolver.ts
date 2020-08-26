import { Store } from '@ngrx/store';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import * as microserviceActions from '../../store/microservice/microservice.actions';
import { MicroservicesState } from 'src/app/store/microservice/mircoservice.state';

@Injectable()
export class MyHubResolver implements Resolve<any> {
    constructor(private microservicesStore: Store<MicroservicesState>) { }

    resolve(route: ActivatedRouteSnapshot): any {
        if (localStorage.getItem('token')) {
            return this.microservicesStore.dispatch(new microserviceActions.LoadAllSubscribedMicroservices([]));
    }
}
}