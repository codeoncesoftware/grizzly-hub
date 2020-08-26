import { User } from 'src/app/shared/models/User';
import { AuthState } from './auth.state';
import * as authActions from './auth.actions';

export const initialAuthState: AuthState = {
    user: new User(),
    githubError: false
};

export function authReducer(state = initialAuthState, action: authActions.UserActions): AuthState {
    switch (action.type) {
        case authActions.LOGIN_USER_SUCCESS:
            const oldState = Object.assign({}, state);
            oldState.user = (action.payload as User);
            return oldState;

        case authActions.GITHOB_LOGIN_ERROR:
            const oldState1 = Object.assign({}, state);
            oldState1.githubError = (action.payload as boolean);
            return oldState1;

        default:
            return state;
    }
}
