import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainLayoutComponent } from './main-layout.component';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { NotFoundComponent } from '../not-found/not-found.component';
import { SettingsComponent } from '../settings/settings.component';
import { DashboardResolver } from 'src/app/shared/resolvers/dashboard.resolver';
import { MicroservicesResolver } from 'src/app/shared/resolvers/all-microservices.resolver';
import { SwaggersResolver } from 'src/app/shared/resolvers/all-swaggers.resolver';
import { NotificationsResolver } from '../../shared/resolvers/AllNotifications.resolver';
import { NotificationListComponent } from '../../notification/notification-list/notification-list.component';
import { MyHubComponent } from '../my-hub/my-hub.component';
import { MyHubResolver } from 'src/app/shared/resolvers/my-hub.resolver';
import { OrganizationResolver } from 'src/app/shared/resolvers/organization.resolver';
import { TeamHubComponent } from '../team-hub/team-hub.component';
const routes: Routes = [
  {
    path: '',
     component: DashboardComponent, resolve: { dashboard: DashboardResolver },
  },
  {
    path: 'app',
    component: MainLayoutComponent,
    resolve: { microservicesList: MicroservicesResolver, notifications: NotificationsResolver, myHub: MyHubResolver, organization: OrganizationResolver },
    children: [
      { path: '', redirectTo: '/app/dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent, resolve: { dashboard: DashboardResolver } },
      { path: 'notification-list', component: NotificationListComponent },
      { path: 'team-hub', component: TeamHubComponent },
      { path: 'settings', component: SettingsComponent },
      { path: 'microservice', loadChildren: '../../microservice/microservice.module#MicroserviceModule' },
      { path: 'organization', loadChildren: '../../organization/organization.module#OrganizationModule', resolve: {} },
      { path: '**', component: NotFoundComponent }
    ]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes, { enableTracing: false })],
  exports: [RouterModule],
  providers: [DashboardResolver, MicroservicesResolver, NotificationsResolver, MyHubResolver, OrganizationResolver]
})
export class MainLayoutRoutingModule { }
