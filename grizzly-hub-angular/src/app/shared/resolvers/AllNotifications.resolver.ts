import {Injectable} from '@angular/core';
import {ActivatedRoute, ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {Store} from '@ngrx/store';
import * as notificationActions from '../../store/notification/notification.actions';
import {NotificationState} from '../../store/notification/notification.state';
import {NotificationService} from '../../notification/notification.service';

@Injectable()
export class NotificationsResolver implements Resolve<any> {
    constructor(private activatedRoute: ActivatedRoute, private store: Store<NotificationState>, private notificationService: NotificationService) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
        if (localStorage.getItem('token')) {
            return this.store.dispatch(new notificationActions.LoadLatestNotifications({}));
        }
    }
}