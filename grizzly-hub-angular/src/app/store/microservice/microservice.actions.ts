import { Action } from '@ngrx/store';
import { Microservice } from '../../shared/models/Microservice';
import { Swagger } from 'src/app/shared/models/Swagger';

export const LOAD_ALL_MICROSERVICES_SUCCESS = '[Microservice] LOAD_ALL_MICROSERVICES_SUCCESS';
export const LOAD_ALL_MICROSERVICES = '[Microservice] LOAD_ALL_MICROSERVICES';
export const LOAD_ALL_SWAGGERS_SUCCESS = '[Swagger] LOAD_ALL_SWAGGERS_SUCCESS';
export const LOAD_SWAGGERS_BY_MICROSERVICE_ID_SUCCESS = '[Swagger] LOAD_SWAGGERS_BY_MICROSERVICE_ID_SUCCESS';
export const LOAD_SWAGGERS_BY_MICROSERVICE_ID = '[Swagger] LOAD_SWAGGERS_BY_MICROSERVICE_ID';
export const LOAD_ALL_SWAGGERS = '[Swagger] LOAD_ALL_SWAGGERS';
export const ADD_MICROSERVICE = '[Microservice] ADD_MICROSERVICE';
export const ADD_MICROSERVICE_SUCCESS = '[Microservice] ADD_MICROSERVICE_SUCCESS';
export const ADD_SWAGGER = '[Swagger] ADD_SWAGGER';
export const ADD_SWAGGER_SUCCESS = '[Swagger] ADD_SWAGGER_SUCCESS';
export const DELETE_MICROSERVICE = '[Microservice] REMOVE_MICROSERVICE';
export const DELETE_MICROSERVICE_SUCCESS = '[Microservice] REMOVE_MICROSERVICE_SUCCESS';
export const UPDATE_MICROSERVICE = '[Microservice] UPDATE_MICROSERVICE';
export const UPDATE_MICROSERVICE_SUCCESS = '[Microservice] UPDATE_MICROSERVICE_SUCCESS';
export const GET_MICROSERVICE = '[Microservice] GET_MICROSERVICE';
export const GET_MICROSERVICE_SUCCESS = '[Microservice] GET_MICROSERVICE_SUCCESS';
export const CHANGE_MICROSERVICES = '[Microservice] CHANGE_MICROSERVICES';
export const LOAD_ALL_SUBSCRIBED_MICROSERVICES = '[Microservice] LOAD_ALL_SUBSCRIBED_MICROSERVICES';
export const LOAD_ALL_SUBSCRIBED_MICROSERVICES_SUCCESS = '[Microservice] LOAD_ALL_SUBSCRIBED_MICROSERVICES_SUCCESS'
export const ADD_MICROSERVICE_ATTACH_FILE = '[Microservice] ADD_MICROSERVICE_ATTACH_FILE';
export const ADD_MICROSERVICE_ATTACH_FILE_REQUEST = '[Microservice] ADD_MICROSERVICE_ATTACH_FILE_REQUEST';
export const UPDATE_MICROSERVICE_ATTACH_FILE = '[Microservice] UPDATE_MICROSERVICE_ATTACH_FILE';
export const UPDATE_MICROSERVICE_ATTACH_FILE_REQUEST = '[Microservice] UPDATE_MICROSERVICE_ATTACH_FILE_REQUEST';
export const LOAD_ALL_MICROSERVICES_FOR_TEAM = '[Microservice] LOAD_ALL_MICROSERVICES_FOR_TEAM';
export const LOAD_ALL_MICROSERVICES_FOR_TEAM_SUCCESS = '[Microservice] LOAD_ALL_MICROSERVICES_FOR_TEAM_SUCCESS';

export class ChangeMicroservice implements Action {
    readonly type: string = CHANGE_MICROSERVICES;
    constructor(public payload: any = {}) { }
}

export class AddMicroserviceAttachFileRequest implements Action {
    readonly type: string = ADD_MICROSERVICE_ATTACH_FILE_REQUEST;
    constructor(public payload: any) { }
}

export class AddMicroserviceAttachFile implements Action {
    readonly type: string = ADD_MICROSERVICE_ATTACH_FILE;
    constructor(public payload: any) { }
}

export class UpdateMicroserviceAttachFileRequest implements Action {
    readonly type: string = UPDATE_MICROSERVICE_ATTACH_FILE_REQUEST;
    constructor(public payload: any) { }
}

export class UpdateMicroserviceAttachFile implements Action {
    readonly type: string = UPDATE_MICROSERVICE_ATTACH_FILE;
    constructor(public payload: any) { }
}

export class AddMicroservice implements Action {
    readonly type: string = ADD_MICROSERVICE;
    constructor(public payload: { microservice: any, swaggers: any[] }) { }
}

export class AddMicroserviceSuccess implements Action {
    readonly type: string = ADD_MICROSERVICE_SUCCESS;
    constructor(public payload: { microservice: any, swaggers: any[] }) { }
}

export class DeleteMicroservice implements Action {
    readonly type: string = DELETE_MICROSERVICE;
    constructor(public payload: string) { }
}

export class DeleteMicroserviceSucess implements Action {
    readonly type: string = DELETE_MICROSERVICE_SUCCESS;
    constructor(public payload: string) { }
}

export class UpdateMicroservice implements Action {
    readonly type: string = UPDATE_MICROSERVICE;
    constructor(public payload: { microservice: any, swaggers: any[] }) { }
}

export class UpdateMicroserviceSuccess implements Action {
    readonly type: string = UPDATE_MICROSERVICE_SUCCESS;
    constructor(public payload: { microservice: any, swaggers: any[] }) { }
}

export class LoadAllMicroservices implements Action {
    readonly type: string = LOAD_ALL_MICROSERVICES;
    constructor(public payload: any = {}) { }
}

export class LoadAllMicroservicesSuccess implements Action {
    readonly type: string = LOAD_ALL_MICROSERVICES_SUCCESS;
    constructor(public payload: Microservice[]) { }
}
export class LoadAllSwaggers implements Action {
    readonly type: string = LOAD_ALL_SWAGGERS;
    constructor(public payload: any = {}) { }
}

export class LoadAllSwaggersSuccess implements Action {
    readonly type: string = LOAD_ALL_SWAGGERS_SUCCESS;
    constructor(public payload: any[]) { }
}

export class GetMicroservice implements Action {
    readonly type = GET_MICROSERVICE;
    constructor(public payload: Microservice) { }
}

export class LoadAllSubscribedMicroservices implements Action {
    readonly type = LOAD_ALL_SUBSCRIBED_MICROSERVICES;
    constructor(public payload: any = {}) { }
}
export class LoadAllSubscribedMicroservicesSuccess implements Action {
    readonly type = LOAD_ALL_SUBSCRIBED_MICROSERVICES_SUCCESS;
    constructor(public payload: Microservice[]) { }
}


export type MicroserviceActions =
    LoadAllMicroservices
    | AddMicroservice
    | AddMicroserviceSuccess
    | DeleteMicroservice
    | DeleteMicroserviceSucess
    | LoadAllMicroservicesSuccess
    | GetMicroservice
    | LoadAllSwaggers
    | LoadAllSwaggersSuccess
    | LoadAllSubscribedMicroservices
    | LoadAllSubscribedMicroservicesSuccess
    | AddMicroserviceAttachFile;
