import { Injectable, Optional, Inject } from '@angular/core';
import { Effect, Actions, ofType } from '@ngrx/effects';
import * as microserviceActions from './microservice.actions';
import * as subscriptionActions from '../subscription/subscription.actions';
import * as globalActions from '../global.actions';
import { switchMap, map, catchError } from 'rxjs/operators';

// MICROSERVICE SERVICE FOR HTTP CALLS
import { MicroserviceService } from '../../microservice/microservice.service';
import { Router } from '@angular/router';
import { Action, Store } from '@ngrx/store';
import { of } from 'rxjs';
import { Microservice } from 'src/app/shared/models/Microservice';
import { ToastrService } from 'ngx-toastr';
import { AppTranslateService } from 'src/app/shared/services/app-translate-service';
import { MicroserviceModalService } from 'src/app/microservice/microservice-modal/microservice-modal.service';
import { DashboardService } from 'src/app/layout/dashboard/dashboard.service';
import { TeamHubService } from '../../layout/team-hub/team-hub.service';
import { SubscriptionState } from '../subscription/subscription.state';

export class EffectError implements Action {
  readonly type = '[Error] Effect Error';
}

@Injectable()
export class MicroserviceEffects {
  constructor(
    private router: Router,
    // tslint:disable: align
    private actions: Actions,
    private microserviceService: MicroserviceService,
    private toaster: ToastrService,
    private translateService: AppTranslateService,
    private microserviceModalService: MicroserviceModalService,
    private dashboardService: DashboardService,
    private teamHubService: TeamHubService,
    public subscriptionStore: Store<SubscriptionState>
  ) // tslint:enable: align
  { }

  @Effect()
  loadMicroservices = this.actions.pipe(
    ofType(microserviceActions.LOAD_ALL_MICROSERVICES),
    switchMap(() => {
      return this.microserviceService.getAllMicroservices(50, 0).pipe(
        map(
          (microservices) =>
            new microserviceActions.LoadAllMicroservicesSuccess(
              microservices.content
            )
        ),
        catchError(() => of(new globalActions.EffectError({})))
      );
    })
  );
  @Effect()
  loadSwaggers = this.actions.pipe(
    ofType(microserviceActions.LOAD_ALL_SWAGGERS),
    switchMap(() => {
      return this.microserviceService.getSwaggers().pipe(
        map(
          (swaggers: any) =>
            new microserviceActions.LoadAllSwaggersSuccess(swaggers.content)
        ),
        catchError(() => of(new globalActions.EffectError({})))
      );
    })
  );

  @Effect()
  createNewMicroserviceAttachFile = this.actions.pipe(
    ofType(microserviceActions.ADD_MICROSERVICE_ATTACH_FILE_REQUEST),
    switchMap((action: microserviceActions.AddMicroserviceAttachFile) => {
      return this.microserviceService
        .addMicroserviceAttachFile(action.payload)
        .pipe(
          map((result: any) => {
            this.toaster.success(
              this.translateService.getMessage('toaster.microservice.added')
            );
            result.microservice.swaggersVersions = result.swaggers;
            const envs = [];
            result.swaggers.forEach((element) => {
              envs.push(element.environment);
            });
            const subscription = {
              microserviceId: result.microservice.id,
              frequence: 1,
              environment: envs,
              changes: ['endpoints', 'codes', 'responses', 'params'],
              userEmail: localStorage.getItem('userEmail'),
            };
            console.log(subscription);

            this.subscriptionStore.dispatch(
              new subscriptionActions.Subscribe(subscription)
            );
            this.microserviceModalService.close();
            this.router.navigate(['/app/microservice/', result.microservice.id]);
            return new microserviceActions.AddMicroserviceSuccess({
              microservice: result.microservice,
              swaggers: result.swaggers,
            });
          }),
          catchError(() => of(new globalActions.EffectError({})))
        );
    })
  );

  @Effect()
  updateNewMicroserviceAttachFile = this.actions.pipe(
    ofType(microserviceActions.UPDATE_MICROSERVICE_ATTACH_FILE_REQUEST),
    switchMap((action: microserviceActions.UpdateMicroserviceAttachFile) => {
      return this.microserviceService
        .updateMicroserviceAttachFile(action.payload)
        .pipe(
          map((result: any) => {
            console.log('result from effect ', result);
            console.log(action.payload);
            this.toaster.success(
              this.translateService.getMessage('toaster.microservice.updated')
            );
            this.microserviceModalService.close();
            console.log(result);
            return new microserviceActions.UpdateMicroserviceSuccess({
              microservice: result.microservice,
              swaggers: result.swaggers,
            });
          }),
          catchError(() => of(new globalActions.EffectError({})))
        );
    })
  );

  @Effect()
  deleteMicroservice = this.actions.pipe(
    ofType(microserviceActions.DELETE_MICROSERVICE),
    switchMap((action: microserviceActions.DeleteMicroservice) => {
      return this.microserviceService
        .deleteMicroserviceById(action.payload)
        .pipe(map((data) => {
          if (!data) {
            this.router.navigate(['/app/dashboard']);
            this.toaster.success(
              this.translateService.getMessage('toaster.microservice.deleted')
            );
            return new microserviceActions.DeleteMicroserviceSucess(action.payload);
          } else {
            this.router.navigate(['/app/microservice/', action.payload]);
          }
        }));

    }),
    catchError(() => of(new globalActions.EffectError({})))
  );


  @Effect()
  loadSubscribedMicroservices = this.actions.pipe(
    ofType(microserviceActions.LOAD_ALL_SUBSCRIBED_MICROSERVICES),
    switchMap((action: microserviceActions.LoadAllSubscribedMicroservices) => {
      return this.dashboardService.getSubscribedMicroservices().pipe(
        map(
          (microservices: Microservice[]) =>
            new microserviceActions.LoadAllSubscribedMicroservicesSuccess(
              microservices
            )
        ),
        catchError(() => of(new globalActions.EffectError({})))
      );
    })
  );

}
