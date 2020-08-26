import { Action } from '@ngrx/store';
import { Swagger } from 'src/app/shared/models/Swagger';

export const LOAD_ALL_SWAGGERS_SUCCESS = '[Swagger] LOAD_ALL_SWAGGERS_SUCCESS';
export const LOAD_ALL_SWAGGERS = '[Swagger] LOAD_ALL_SWAGGERS';
export const GET_SWAGGER_DIFFS_REQUEST = '[Swagger] GET_SWAGGER_DIFFS_REQUEST';
export const GET_SWAGGER_DIFFS_SUCCESS = '[Swagger] GET_SWAGGER_DIFFS_SUCCESS';


export class LoadAllSwaggers implements Action {
    readonly type: string = LOAD_ALL_SWAGGERS;
    constructor(public payload:any = {}) {}
}

export class LoadAllSwaggersSuccess implements Action {
    readonly type: string = LOAD_ALL_SWAGGERS_SUCCESS;
    constructor(public payload:Swagger[]) {}
}

export class SwaggerDiffsRequest implements Action {
    readonly type: string =GET_SWAGGER_DIFFS_REQUEST;
    constructor(public payload:any = {}) {}
}

export type SwaggerActions = LoadAllSwaggers | LoadAllSwaggersSuccess;
