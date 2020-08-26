import {Action} from '@ngrx/store';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {act, Actions, Effect, ofType} from '@ngrx/effects';
import {NotificationService} from '../../notification/notification.service';
import * as notificationActions from './notification.actions';
import {catchError, map, switchMap} from 'rxjs/operators';
import {of} from 'rxjs';
import * as globalActions from '../global.actions';
import * as subscriptionActions from '../subscription/subscription.actions';



export class EffectError implements Action {
    readonly type = '[Error] Effect Error';
}

@Injectable()
export class NotificationEffect {

    constructor(private router: Router,
                // tslint:disable: align
                private actions: Actions,
                private notificationService: NotificationService,
                // tslint:enable: align
    ) {
    }

    @Effect()
    loadNotifications = this.actions.pipe(
        ofType(notificationActions.LOAD_ALL_NOTIFICATIONS),
        switchMap(() => {
            return this.notificationService.getAllNotifications().pipe(
                map(notifications => new notificationActions.LoadAllNotificationsSuccess(notifications)),
                catchError(() => of(new globalActions.EffectError({})))
            );
        }),
    );

    @Effect()
    deleteNotifications = this.actions.pipe(
        ofType(notificationActions.DELETE_NOTIFICATIONS),
        map((action: notificationActions.DeleteNotifications)=>{
            this.notificationService.deleteAllNotifications();
            return new notificationActions.DeleteNotificationsSuccess();
        }),
        catchError(() => of(new globalActions.EffectError({})))
    );

    @Effect()
    seeNotifications = this.actions.pipe(
        ofType(notificationActions.SEE_NOTIFICATIONS),
        map((action: notificationActions.SeeNotifications)=>{
            if (localStorage.getItem('token')) {
                this.notificationService.allNotificationsSeen();
                return new notificationActions.SeeNotificationsSuccess();
            }
        }),
        catchError(() => of(new globalActions.EffectError({})))
    );

    @Effect()
    loadLatestNotifications = this.actions.pipe(
        ofType(notificationActions.LOAD_LATEST_NOTIFICATIONS),
        switchMap(()=>{
            return this.notificationService.getLatestNotifications().pipe(
                map(notifications => new notificationActions.LoadLatestNotificationsSuccess(notifications)),
                catchError(() => of(new globalActions.EffectError({})))
            );
            }
        )
    )

    @Effect()
    deleteNotification = this.actions.pipe(
        ofType(notificationActions.DELETE_NOTIFICATION),
        switchMap((action: notificationActions.DeleteNotification)=>{
            return this.notificationService.deleteNotification(action.payload).pipe(
                map(
                    data => {
                        return new notificationActions.DeleteNotificationSuccess(action.payload);
                    }
                )
            )
        }),
        catchError(() => of(new globalActions.EffectError({})))
    )


}
