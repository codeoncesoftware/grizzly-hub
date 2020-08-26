import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { MicroserviceComponent } from './microservice.component';
import { MyHubComponent } from '../layout/my-hub/my-hub.component';
import { DashboardResolver } from '../shared/resolvers/dashboard.resolver';
import { MyHubResolver } from '../shared/resolvers/my-hub.resolver';
import { TeamHubComponent } from '../layout/team-hub/team-hub.component';

const routes: Routes = [
    {
        path: 'myhub',
        component: MyHubComponent,
        resolve: {
            dashboard: DashboardResolver,
            myHub: MyHubResolver
        }
    },
    {
        path: 'team-hub',
        component: TeamHubComponent,
        resolve: {
            dashboard: DashboardResolver,
            myHub: MyHubResolver
        }
    },
    {
        path: ':id',
        component: MicroserviceComponent
    },
    {
        path: ':id/:swaggerId',
        component: MicroserviceComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    providers: []
})
export class MicroserviceRoutingModule { }