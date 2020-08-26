import {NotificationState} from './notification.state';
import * as notification from './notification.actions';
import {Notification} from '../../shared/models/Notification';
import {not} from 'rxjs/internal-compatibility';


export const initialNotificationState: NotificationState = {
    notifications: [],
};

export function notificationReducer(state = initialNotificationState, action: notification.notificationActions): NotificationState {
    switch (action.type) {

        case notification.LOAD_ALL_NOTIFICATIONS_SUCCESS:
            const stateAllNotificationsLoaded = Object.assign({}, state);
            stateAllNotificationsLoaded.notifications = (action.payload as Notification[]);
            return stateAllNotificationsLoaded;

        case notification.ADD_NOTIFICATIONS:
            const stateToReturn = Object.assign({}, state);
            stateToReturn.notifications.unshift(action.payload);
            console.log(stateToReturn)
            return stateToReturn;

        case notification.DELETE_ALL_NOTIFICATIONS_SUCCESS:
            const newState = Object.assign({}, state);
            newState.notifications.length =0;
            return newState;

        case notification.SEE_NOTIFICATIONS_SUCCESS:
            const newStateToReturn = Object.assign({}, state);
            newStateToReturn.notifications.forEach(notif => {
                notif.opened = true;
            });
            return newStateToReturn;

        case notification.LOAD_LATEST_NOTIFICATIONS_SUCCESS:
            const stateLatestNotificationsLoaded = Object.assign({}, state);
            stateLatestNotificationsLoaded.notifications = (action.payload as Notification[]);
            return stateLatestNotificationsLoaded;

        case notification.DELETE_NOTIFICATION_SUCCESS:
            const stateNotificationDeleted = Object.assign({}, state);
            stateNotificationDeleted.notifications.forEach((notif, index) =>{
                if(notif.id === action.payload){
                    stateNotificationDeleted.notifications.splice(index, 1);
                }
            });
            return stateNotificationDeleted;

        default:
            return state;
    }
}