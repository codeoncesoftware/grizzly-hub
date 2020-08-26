import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { AuthResolver } from './shared/resolvers/auth.resolver';
import { LoginComponent } from './auth/login/login.component';
import { MicroservicesResolver } from './shared/resolvers/all-microservices.resolver';
import { GithubLoginComponent } from './auth/github-login/github-login.component';
import { GithubResolver } from './shared/resolvers/github-resolver';
import { SwaggersResolver } from './shared/resolvers/all-swaggers.resolver';
import {NotificationsResolver} from './shared/resolvers/AllNotifications.resolver';
import { MyHubResolver } from './shared/resolvers/my-hub.resolver';
import { OrganizationResolver } from './shared/resolvers/organization.resolver';
import { DashboardResolver } from './shared/resolvers/dashboard.resolver';
import { DashboardComponent } from './layout/dashboard/dashboard.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  { path: 'github/login', component: GithubLoginComponent, resolve: { githubResolver: GithubResolver } },
  { path: 'confirm/email/:token', component: LoginComponent, resolve: { authResolver: AuthResolver } },
  { path: 'app', component: MainLayoutComponent, resolve: { microservicesList: MyHubResolver ,  notifications: NotificationsResolver , organization : OrganizationResolver , dashboard : DashboardResolver  } },
  { path: 'home', component: MainLayoutComponent, resolve: { microservicesList: MyHubResolver ,  notifications: NotificationsResolver , organization : OrganizationResolver , dashboard : DashboardResolver  } },
  { path: 'microservices', loadChildren: () => import('./microservice/microservice.module').then(m => m.MicroserviceModule), resolve: {} },
  { path: 'organization', loadChildren: () => import('./organization/organization.module').then(m => m.OrganizationModule) , resolve : { } },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AuthResolver, MicroservicesResolver, GithubResolver, SwaggersResolver , NotificationsResolver ,MyHubResolver , OrganizationResolver , DashboardResolver]
})

export class AppRoutingModule { }
