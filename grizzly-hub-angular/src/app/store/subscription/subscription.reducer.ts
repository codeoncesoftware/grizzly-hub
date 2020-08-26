import { SubscriptionState } from './subscription.state';
import * as subscriptions from './subscription.actions';
import { SubscriptionDto } from 'src/app/shared/models/Subscription';
import { MicroservicesState } from '../microservice/mircoservice.state';
import { Microservice } from 'src/app/shared/models/Microservice';





export const initialSubscriptionState: SubscriptionState = {
    subscriptions: [],
};
export const initialMicroserviceState : MicroservicesState = {
    microservices: [],
    active: new Microservice(),
    success: false,
    swaggers: [],
    subscribed: [],
    publique : [],
    shared : [],
    personnal : []

}

export function subscriptionReducer(state = initialSubscriptionState,  action: subscriptions.SubscriptionActions): SubscriptionState {
    switch (action.type) {

        case subscriptions.LOAD_SUBSCRIPTIONS_SUCCESS:
            const stateSubscriptionsLoaded = Object.assign({}, state);
            stateSubscriptionsLoaded.subscriptions = (action.payload as any[]);
            return stateSubscriptionsLoaded;

        case subscriptions.SUBSCRIPTION_SUCCESS:
            const stateToReturn = Object.assign({}, state);
           // const stateMicroservice = Object.assign({} , secondState);
            stateToReturn.subscriptions.push(action.payload);
           // stateMicroservice.subscribed.push(action.payload);
            return stateToReturn;

        case subscriptions.DELETE_SUBSCRIPTION_SUCCESS:
            const stateAfterDelete = Object.assign({}, state);
            stateAfterDelete.subscriptions = stateAfterDelete.subscriptions.filter((subscription) => subscription.id !== action.payload);

            return stateAfterDelete;
        case subscriptions.UPDATE_SUBSCRIPTION:
            const stateAfterUpdate = Object.assign({}, state);
            stateAfterUpdate.subscriptions = stateAfterUpdate.subscriptions.filter((subscription) => subscription.microserviceId !== action.payload.microserviceId);

            return stateAfterUpdate;

        default:
            return state;
    }
}
