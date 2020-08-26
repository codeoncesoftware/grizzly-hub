import { Injectable, Optional, Inject } from '@angular/core';
import { Effect, Actions, ofType } from '@ngrx/effects';
import * as organizationActions from './organization.actions';
import * as globalActions from '../global.actions';
import { switchMap, map, catchError } from 'rxjs/operators';

// PROJECT SERVICE FOR HTTP CALLS
import { Router } from '@angular/router';
import { Action } from '@ngrx/store';
import { of } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { AppTranslateService } from 'src/app/shared/services/app-translate-service';
import { OrganizationService } from 'src/app/organization/organization-menu/organization.service';
import { OrganizationModalService } from 'src/app/organization/organization-modal/organization-modal.service';


export class EffectError implements Action {
    readonly type = '[Error] Effect Error';
}

@Injectable()
export class OrganizationEffects {

    constructor(private router: Router,
        // tslint:disable: align
        private actions: Actions,
        private organizationService: OrganizationService,
        private toaster: ToastrService,
        private translateService: AppTranslateService,
        public organizationModal: OrganizationModalService
        // tslint:enable: align
    ) { }

    @Effect()
    loadOrganization = this.actions.pipe(
        ofType(organizationActions.LOAD_ALL_ORGANIZATIONS),
        switchMap(() => {
            return this.organizationService.getOrganisation().pipe(
                map((organization) => new organizationActions.LoadAllOrganizationsSuccess(organization)),
                catchError(() => of(new globalActions.EffectError({})))
            );
        }),
    );

    @Effect()
    createNewOrganization = this.actions.pipe(
        ofType(organizationActions.ADD_ORGANIZATION),
        switchMap((action: organizationActions.AddOrganization) => {
            console.log('pp');
            return this.organizationService.addOrganisation(action.payload).pipe(

                map((result: any) => {

                    console.log(result);
                    this.toaster.success(this.translateService.getMessage('toaster.organization.added'));
                    this.organizationModal.close();
                    this.router.navigate(['/app/organization/', result.id, 'details']);
                    return new organizationActions.AddOrganizationSuccess(result);
                }),
                catchError(() => of(new globalActions.EffectError({})))
            );
        }
        ));

    @Effect()
    updateOrganization = this.actions.pipe(
        ofType(organizationActions.UPDATE_ORGANIZATION),
        switchMap((action: organizationActions.UpdateOrganization) => {
            console.log('heyyy')
            return this.organizationService.updateOrganization(action.payload.organization, action.payload.id).pipe(
                map(
                    (result: any) => {
                        action.payload.organization.lastUpdate = new Date().toUTCString();
                        this.toaster.success(this.translateService.getMessage('toaster.organization.updated'));
                        return new organizationActions.UpdateOrganizationSuccess(result);
                    }));
        }),
        catchError(() => of(new globalActions.EffectError({})))
    );

    @Effect()
    deleteOrganization = this.actions.pipe(
        ofType(organizationActions.DEELTE_ORGANIZATION),
        map((action: organizationActions.DeleteOrganization) => {
            this.organizationService.deleteOrganisation(action.payload).subscribe((data) => {
                if (!data) {
                    this.router.navigate(['/app/dashboard']);
                    this.toaster.success(this.translateService.getMessage('toaster.organization.deleted'));
                } else {
                    this.router.navigate(['/app/organization/', action.payload, '/details']);
                }
            });
            return new organizationActions.DeleteOrganizationSuccess(action.payload);
        }),
        catchError(() => of(new globalActions.EffectError({})))
    );

}