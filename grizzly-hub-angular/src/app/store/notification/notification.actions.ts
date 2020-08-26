import {Action} from '@ngrx/store';
import {Notification} from '../../shared/models/Notification';



export const LOAD_ALL_NOTIFICATIONS = '[Notification] LOAD_ALL_NOTIFICATIONS'
export const LOAD_ALL_NOTIFICATIONS_SUCCESS = '[Notification] LOAD_ALL_NOTIFICATIONS_SUCCESS'
export const ADD_NOTIFICATIONS = '[Notification] ADD_NOTIFICATIONS'
export const DELETE_NOTIFICATIONS = '[Notification] DELETE_NOTIFICATIONS'
export const DELETE_ALL_NOTIFICATIONS_SUCCESS = '[Notification] DELETE_ALL_NOTIFICATIONS_SUCCESS'
export const SEE_NOTIFICATIONS = '[Notification] SEE_NOTIFICATIONS'
export const SEE_NOTIFICATIONS_SUCCESS = '[Notification] SEE_NOTIFICATIONS_SUCCESS'
export const LOAD_LATEST_NOTIFICATIONS = '[Notification] LOAD_LATEST_NOTIFICATIONS'
export const LOAD_LATEST_NOTIFICATIONS_SUCCESS = '[Notification] LOAD_LATEST_NOTIFICATIONS_SUCCESS'
export const DELETE_NOTIFICATION = '[Notification] DELETE_NOTIFICATION'
export const DELETE_NOTIFICATION_SUCCESS = '[Notification] DELETE_NOTIFICATION_SUCCESS'

export class LoadAllNotifications implements Action {
    readonly type: string = LOAD_ALL_NOTIFICATIONS;

    constructor(public payload: any={}){}

}

export class LoadAllNotificationsSuccess implements Action {
    readonly type: string = LOAD_ALL_NOTIFICATIONS_SUCCESS;
    constructor(public payload: Notification[]) { }
}

export class AddNotifications implements Action {
    readonly type: string = ADD_NOTIFICATIONS;
    constructor(public payload: Notification) {}
}

export class DeleteNotifications implements Action {
    readonly type: string = DELETE_NOTIFICATIONS;
    constructor(public payload: void) {
    }
}

export class DeleteNotificationsSuccess implements Action{
    readonly type: string = DELETE_ALL_NOTIFICATIONS_SUCCESS;
    constructor(public payload: void) {
    }
}

export class SeeNotifications implements Action {
    readonly  type: string = SEE_NOTIFICATIONS;
    constructor(public payload: void) {
    }
}

export class SeeNotificationsSuccess implements Action {
    readonly type: string = SEE_NOTIFICATIONS_SUCCESS;
    constructor(public payload:void) {
    }
}

export  class LoadLatestNotifications implements Action {
    readonly type: string = LOAD_LATEST_NOTIFICATIONS;
    constructor(
        public payload: any={}
    ) {
    }
}

export class LoadLatestNotificationsSuccess implements Action{
    readonly type: string = LOAD_LATEST_NOTIFICATIONS_SUCCESS;
    constructor(public payload: Notification[]) {
    }
}

export class DeleteNotification implements  Action{
    readonly  type: string = DELETE_NOTIFICATION;
    constructor(public payload: string) {
    }
}

export class DeleteNotificationSuccess implements Action {
    readonly type: string = DELETE_NOTIFICATION_SUCCESS;
    constructor(public payload: string) {
    }
}

export type notificationActions = LoadAllNotifications | LoadAllNotificationsSuccess | DeleteNotifications | LoadLatestNotifications | LoadLatestNotificationsSuccess | DeleteNotification | DeleteNotificationSuccess;