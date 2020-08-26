import * as swaggerActions from './swagger.actions';
import * as globalActions from '../global.actions';
import { SwaggerState } from './swagger.state';
import { Microservice } from 'src/app/shared/models/Microservice';
import { Swagger } from 'src/app/shared/models/Swagger';

export const initialSwaggerState: SwaggerState = {
    swaggers: [],
    active: new Swagger(),
    success: false
};

export function swaggerReducer(state = initialSwaggerState, action: swaggerActions.SwaggerActions): SwaggerState {
    switch (action.type) {

        case swaggerActions.LOAD_ALL_SWAGGERS_SUCCESS:
            return { ...state, swaggers: action.payload as Swagger[] };

        case swaggerActions.LOAD_ALL_SWAGGERS:
            return { ...state, success: false }

        case globalActions.EFFECT_ERROR:
            // return Object.assign({}, state);
            return { ...state };
        default:
            return state;
    }
}
