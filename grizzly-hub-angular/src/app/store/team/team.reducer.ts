import * as teamActions from './team.actions';
import * as globalActions from '../global.actions';
import { TeamsState } from './team.state';
import { Team } from 'src/app/shared/models/Team';


export const initialTeamState: TeamsState = {
    team: [],
    success: false,
};

export function teamReducer(state = initialTeamState, action: teamActions.TeamActions): TeamsState {
    switch (action.type) {

        case teamActions.UPDATE_TEAM_SUCCESS:
            console.log('reducer')
            console.log('state')
            const stateUpdated = Object.assign({}, state);
            stateUpdated.team[0] = (action.payload);
            return stateUpdated;

        case globalActions.EFFECT_ERROR:
            console.log('he')
            return Object.assign({}, state);

        default:
            console.log('def');
            return state;
    }
}