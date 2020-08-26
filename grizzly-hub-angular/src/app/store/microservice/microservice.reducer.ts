import * as microservice from './microservice.actions';
import * as globalActions from '../global.actions';
import { MicroservicesState } from './mircoservice.state';
import { Microservice } from 'src/app/shared/models/Microservice';
import _ from 'lodash';
import { act } from '@ngrx/effects';

export const initialMicroserviceState: MicroservicesState = {
    microservices: [],
    active: new Microservice(),
    success: false,
    swaggers: [],
    subscribed: [],
    publique: [],
    shared: [],
    personnal: []
};

export function microserviceReducer(state = initialMicroserviceState, action: microservice.MicroserviceActions): MicroservicesState {
    switch (action.type) {

        case microservice.LOAD_ALL_SUBSCRIBED_MICROSERVICES_SUCCESS:
            const subscribedMicroservices = Object.assign({}, state);
            subscribedMicroservices.subscribed = (action.payload as Microservice[]);
            subscribedMicroservices.success = true;
            return subscribedMicroservices;

        case microservice.LOAD_ALL_MICROSERVICES_SUCCESS:
            const stateAllMicroservicesLoaded = Object.assign({}, state);
            stateAllMicroservicesLoaded.microservices = (action.payload as Microservice[]);
            stateAllMicroservicesLoaded.publique = [];
            stateAllMicroservicesLoaded.shared = [];
            stateAllMicroservicesLoaded.personnal = [];
            action.payload.forEach(element => {

                if (element.type === 'public') {
                    if (!stateAllMicroservicesLoaded.publique.some(el => el.id === element.id)) {
                    stateAllMicroservicesLoaded.publique.push(element)
                }
                }
                if ((element.type === 'private' && element.owner === localStorage.getItem('userEmail'))) {
                    if (!stateAllMicroservicesLoaded.personnal.some(el => el.id === element.id)) {
                    stateAllMicroservicesLoaded.personnal.push(element)
                }
                }
                if (element.type === 'shared' && _.intersection(JSON.parse(localStorage.getItem('myTeamsIds')), element.teamIds).length > 0) {
                    if (!stateAllMicroservicesLoaded.shared.some(el => el.id === element.id)) {
                        stateAllMicroservicesLoaded.shared.push(element)
                    }
                }

            });
            return stateAllMicroservicesLoaded;
        case microservice.CHANGE_MICROSERVICES:
            const microservices = Object.assign({}, state);
            microservices.microservices = (action.payload as Microservice[]);
            action.payload.forEach(element => {

                if (element.publique === true && !microservices.publique.some(ms => ms === element)) {
                    microservices.publique.push(element)
                }
                if ((element.type === 'private' && element.owner === localStorage.getItem('userEmail'))) {
                    if (!microservices.personnal.some(ms => ms === element))
                        microservices.personnal.push(element)
                }
                if (element.type === 'shared' && !microservices.shared.some(ms => ms === element) && _.intersection(JSON.parse(localStorage.getItem('myTeamsIds')), element.teamIds).length > 0) {
                    if (!microservices.shared.some(el => el.id === element.id)) {
                        microservices.shared.push(element)
                    }
                }

            });
            microservices.success = true;
            return microservices;

        case microservice.ADD_MICROSERVICE_SUCCESS:
            const stateToReturn = Object.assign({}, state);
            stateToReturn.microservices.push((action.payload.microservice as Microservice));
            console.log(action.payload)
            action.payload.swaggers.forEach(swagger => {
                stateToReturn.swaggers.push(swagger);
            });
            if (action.payload.microservice.type === 'public') {
                stateToReturn.publique.push(action.payload.microservice)
            }
            if ((action.payload.microservice.type === 'private') || (action.payload.microservice.type === 'public' && action.payload.microservice.owner === localStorage.getItem('userEmail'))) {
                stateToReturn.personnal.push(action.payload.microservice)
            }
            if (action.payload.microservice.type === 'shared') {
                stateToReturn.shared.push(action.payload.microservice)
            }
            stateToReturn.subscribed.push(action.payload.microservice);
            return stateToReturn;

        case microservice.UPDATE_MICROSERVICE_SUCCESS:
            const stateActiveUpdated = Object.assign({}, state);
            console.log(action.payload.microservice)
            const index = stateActiveUpdated.microservices.findIndex(ms => ms.id === (action.payload.microservice as Microservice).id);
            const subsribedIndex = stateActiveUpdated.subscribed.findIndex(ms => ms.id === (action.payload.microservice as Microservice).id);
            const publiqueIndex = stateActiveUpdated.publique.findIndex(ms => ms.id === (action.payload.microservice as Microservice).id);
            const sahredIndex = stateActiveUpdated.shared.findIndex(ms => ms.id === (action.payload.microservice as Microservice).id);
            const personnalIndex = stateActiveUpdated.personnal.findIndex(ms => ms.id === (action.payload.microservice as Microservice).id);
            stateActiveUpdated.microservices[index] = (action.payload.microservice as Microservice);
            stateActiveUpdated.subscribed[subsribedIndex] = (action.payload.microservice as Microservice);
            if (stateActiveUpdated.shared.findIndex((e => e.id === action.payload.microservice.id)) >= 0) {
                stateActiveUpdated.shared[sahredIndex] = (action.payload.microservice as Microservice);
                stateActiveUpdated.shared[sahredIndex].swaggersVersions = (action.payload.swaggers);
            }
            if (stateActiveUpdated.personnal.findIndex((e => e.id === action.payload.microservice.id)) >= 0) {
                stateActiveUpdated.personnal[personnalIndex] = (action.payload.microservice as Microservice);
                stateActiveUpdated.personnal[personnalIndex].swaggersVersions = (action.payload.swaggers);
            }
            if (stateActiveUpdated.publique.findIndex((e => e.id === action.payload.microservice.id)) >= 0) {
                stateActiveUpdated.publique[publiqueIndex] = (action.payload.microservice as Microservice);
                stateActiveUpdated.publique[publiqueIndex].swaggersVersions = (action.payload.swaggers);
            }
            switch (action.payload.microservice.type) {
                case 'public':

                    if (sahredIndex >= 0) {
                        stateActiveUpdated.shared.splice(sahredIndex, 1)
                    }
                    if (personnalIndex >= 0) {
                        stateActiveUpdated.personnal.splice(personnalIndex, 1)
                    }
                    if (publiqueIndex < 0) {
                        stateActiveUpdated.publique.push(action.payload.microservice);
                    }
                    break;
                case 'private':
                    if (publiqueIndex >= 0) {
                        stateActiveUpdated.publique.splice(publiqueIndex, 1)
                    }
                    if (sahredIndex >= 0) {
                        stateActiveUpdated.shared.splice(sahredIndex, 1)
                    }
                    if (personnalIndex < 0) {
                        stateActiveUpdated.personnal.push(action.payload.microservice)
                    }
                    break;
                case 'shared':
                    if (publiqueIndex >= 0) {
                        stateActiveUpdated.publique.splice(publiqueIndex, 1)
                    }
                    if (personnalIndex >= 0) {
                        stateActiveUpdated.personnal.splice(personnalIndex, 1)
                    }
                    if (sahredIndex < 0) {
                        stateActiveUpdated.shared.push(action.payload.microservice);
                    }
                    break;

                default:
                    break;
            }
            stateActiveUpdated.swaggers.forEach(swagger => {
                if (swagger.microserviceId === action.payload.microservice.id) {
                    const i = stateActiveUpdated.swaggers.findIndex(ms => ms.id === swagger.id);
                    stateActiveUpdated.swaggers.splice(i, 1);
                }
            });

            action.payload.swaggers.forEach(element => {
                stateActiveUpdated.swaggers.push(element);
            });
            console.log(stateActiveUpdated)
            return stateActiveUpdated;

        case microservice.DELETE_MICROSERVICE_SUCCESS:
            const stateAfterDelete = Object.assign({}, state);
            stateAfterDelete.microservices = stateAfterDelete.microservices.filter((ms) => ms.id !== action.payload);
            stateAfterDelete.publique = stateAfterDelete.publique.filter((ms) => ms.id !== action.payload);
            stateAfterDelete.shared = stateAfterDelete.shared.filter((ms) => ms.id !== action.payload);
            stateAfterDelete.personnal = stateAfterDelete.personnal.filter((ms) => ms.id !== action.payload);
            return stateAfterDelete;



        case globalActions.EFFECT_ERROR:
            return Object.assign({}, state);

        default:
            return state;
    }
}
