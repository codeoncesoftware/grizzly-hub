import { Store } from '@ngrx/store';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import * as dashboardActions from '../../store/subscription/subscription.actions';
import { DashboardState } from 'src/app/store/dashboard/dashboard.state';
import { MicroservicesState } from 'src/app/store/microservice/mircoservice.state';
import { SubscriptionState } from 'src/app/store/subscription/subscription.state';

@Injectable()
export class DashboardResolver implements Resolve<any> {
    constructor(private subscriptionsStore: Store<SubscriptionState>) { }

    resolve(route: ActivatedRouteSnapshot): any {
        if (localStorage.getItem('token')) {
        return this.subscriptionsStore.dispatch(new dashboardActions.LoadSubscriptions([]));
        }
    }
}
