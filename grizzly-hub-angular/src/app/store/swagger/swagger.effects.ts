import { Injectable } from '@angular/core';
import { Effect, Actions, ofType } from '@ngrx/effects';
import * as swaggerActions from './swagger.actions';
import * as globalActions from '../global.actions';
import { switchMap, map, catchError } from 'rxjs/operators';

// PROJECT SERVICE FOR HTTP CALLS
import { Router } from '@angular/router';
import { Action } from '@ngrx/store';
import { of } from 'rxjs';
import { SwaggerService } from '../../swagger/swagger-list/swagger.service';
export class EffectError implements Action {
  readonly type = '[Error] Effect Error';
}

@Injectable()
export class SwaggerEffects {

  constructor(private router: Router,
    // tslint:disable: align
    private actions: Actions,
    private swaggerService: SwaggerService,

    // tslint:enable: align
  ) { }

  @Effect()
  loadSwaggers = this.actions.pipe(
    ofType(swaggerActions.LOAD_ALL_SWAGGERS),
    switchMap(() => {
      return this.swaggerService.getAllSwaggers().pipe(
        map(swaggers => new swaggerActions.LoadAllSwaggersSuccess(swaggers)),
        catchError(() => of(new globalActions.EffectError({})))
      );
    }),
  );
}
