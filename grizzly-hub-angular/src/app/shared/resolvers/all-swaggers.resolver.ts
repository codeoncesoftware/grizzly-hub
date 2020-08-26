import { Store } from '@ngrx/store';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, ActivatedRoute } from '@angular/router';
import { MicroservicesState } from '../../store/microservice/mircoservice.state';
import * as microserviceActions from '../../store/microservice/microservice.actions';


@Injectable()
export class SwaggersResolver implements Resolve<any> {
    constructor(private activatedRoute: ActivatedRoute, private store: Store<MicroservicesState>) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
        if (localStorage.getItem('token')) {
            return this.store.dispatch(new microserviceActions.LoadAllSwaggers({}));
        }
    }
}
