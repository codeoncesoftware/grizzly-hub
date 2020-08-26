import { Action } from '@ngrx/store';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { act, Actions, Effect, ofType } from '@ngrx/effects';
import * as subscriptionActions from './subscription.actions'
import { catchError, map, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import * as globalActions from '../global.actions';
import { SubscriptionService } from '../../layout/dashboard/subscription-details-modal/subscription.service';
import { ToastrService } from 'ngx-toastr';
import { AppTranslateService } from 'src/app/shared/services/app-translate-service';



export class EffectError implements Action {
    readonly type = '[Error] Effect Error';
}

@Injectable()
export class SubscriptionEffect {

    constructor(private router: Router,
        // tslint:disable: align
        private actions: Actions,
        private subscriptionService: SubscriptionService,
        private toaster: ToastrService,
        private translateService: AppTranslateService,
        // tslint:enable: align
    ) { }

    @Effect()
    loadSubscriptions = this.actions.pipe(
        ofType(subscriptionActions.LOAD_SUBSCRIPTIONS),
        switchMap(() => {
            return this.subscriptionService.getSubscriptions().pipe(
                map((subscriptions: any[]) => new subscriptionActions.LoadSubscriptionsSuccess(subscriptions)),
                catchError(() => of(new globalActions.EffectError({})))
            );
        }),
    );

    @Effect()
    Subscribe = this.actions.pipe(
        ofType(subscriptionActions.SUBSCRIPTION),
        switchMap((action: subscriptionActions.Subscribe) => {
            return this.subscriptionService.sendSubscription(action.payload).pipe(map((data) => {
                this.toaster.success(this.translateService.getMessage('toaster.subscription.added'));
                return new subscriptionActions.SubscribeSuccess(data);
            })
            )
        }),
        catchError(() => of(new globalActions.EffectError({})))
    );

    @Effect()
    deleteSubscription = this.actions.pipe(
        ofType(subscriptionActions.DELETE_SUBSCRIPTION),
        map((action: subscriptionActions.DeleteSubscription) => {
            this.subscriptionService.deleteSubscription(action.payload).subscribe((data) => {
                if (!data) {
                    this.router.navigate(['/app/dashboard']);
                    this.toaster.success(this.translateService.getMessage('toaster.subscription.deleted'));
                }

            });
            return new subscriptionActions.DeleteSubscriptionSucess(action.payload);
        }),
        catchError(() => of(new globalActions.EffectError({})))
    );

}