import { Action } from '@ngrx/store';
import { SubscriptionDto } from '../../shared/models/Subscription';



export const SUBSCRIPTION = '[Subscription] SUBSCRIPTION';
export const SUBSCRIPTION_SUCCESS = '[Subscription] SUBSCRIPTION_SUCCESS'
export const DELETE_SUBSCRIPTION = '[Subscription] DELETE_SUBSCRIPTION';
export const DELETE_SUBSCRIPTION_SUCCESS = '[Subscription] DELETE_SUBSCRIPTION_SUCCESS'
export const LOAD_SUBSCRIPTIONS = '[Subscription] LOAD_SUBSCRIPTIONS';
export const LOAD_SUBSCRIPTIONS_SUCCESS = '[Subscription] LOAD_SUBSCRIPTIONS_SUCCESS';
export const UPDATE_SUBSCRIPTION = '[Subscription] UPDATE_SUBSCRIPTION';

export class LoadSubscriptions implements Action {
    readonly type: string = LOAD_SUBSCRIPTIONS;
    constructor(public payload: any[]) { }
}

export class LoadSubscriptionsSuccess implements Action {
    readonly type: string = LOAD_SUBSCRIPTIONS_SUCCESS;
    constructor(public payload: any[]) { }
}
export class Subscribe implements Action {
    readonly type: string = SUBSCRIPTION;
    constructor(public payload: any) { }
}
export class SubscribeSuccess implements Action {
    readonly type: string = SUBSCRIPTION_SUCCESS;
    constructor(public payload: any) { }
}
export class DeleteSubscription implements Action {
    readonly type: string = DELETE_SUBSCRIPTION;
    constructor(public payload: string) { }
}

export class DeleteSubscriptionSucess implements Action {
    readonly type: string = DELETE_SUBSCRIPTION_SUCCESS;
    constructor(public payload: string) { }
}

export class UpdateSubscription implements Action {
    readonly  type: string = UPDATE_SUBSCRIPTION;
    constructor(public payload: any) {
    }
}

export type SubscriptionActions = LoadSubscriptions | LoadSubscriptionsSuccess | Subscribe | SubscribeSuccess | DeleteSubscription | DeleteSubscriptionSucess | UpdateSubscription;
