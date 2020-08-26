import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { OrganizationDetailsComponent } from './organization-details/organization-details.component';
import { OrganizationMembersComponent } from './organization-members/organization-members.component';
import { OrganizationTeamsComponent } from './organization-teams/organization-teams.component';
import { TeamDetailsComponent } from './organization-teams/team-details/team-details.component';

const routes: Routes = [
    {
        path: ':id/details',
        component: OrganizationDetailsComponent
    },
    {
        path: ':id/teams',
        component: OrganizationTeamsComponent
    },
    {
        path: ':id/teams/:id',
        component: TeamDetailsComponent
    },
    {
        path: ':id/members',
        component: OrganizationMembersComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
    providers: []
})
export class OrganizationRoutingModule { }