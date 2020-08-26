import { Action } from '@ngrx/store';
import { User } from 'src/app/shared/models/User';

export const ADD_USER = '[User] ADD_USER';
export const LOGIN_USER = '[User] LOGIN_USER';
export const LOGIN_USER_SUCCESS = '[User] LOGIN_USER_SUCCESS';
export const GITHOB_LOGIN_ERROR = '[User] GITHOB_LOGIN_ERROR';

export class AddUser implements Action {
    readonly type: string = ADD_USER;
    constructor(public payload: User) {}
}

export class LoginUser implements Action {
    readonly type: string = LOGIN_USER;
    constructor(public payload: string) {}
}

export class LoginUserSuccess implements Action {
    readonly type: string = LOGIN_USER_SUCCESS;
    constructor(public payload: User) {}
}

export class LoginGithubError implements Action {
    readonly type: string = GITHOB_LOGIN_ERROR;
    constructor(public payload: boolean) {}
}

export type UserActions = AddUser | LoginUser | LoginUserSuccess | LoginGithubError;
