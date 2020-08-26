import { Store } from '@ngrx/store';
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import * as organizationActions from '../../store/organization/organization.actions';
import { OrganizationsState } from 'src/app/store/organization/organization.state';

@Injectable()
export class OrganizationResolver implements Resolve<any> {
    constructor(private organizationStore: Store<OrganizationsState>) { }

    resolve(route: ActivatedRouteSnapshot): any {
        if (localStorage.getItem('token')) {
            return this.organizationStore.dispatch(new organizationActions.LoadAllOrganizations([]));
        }
    }
}