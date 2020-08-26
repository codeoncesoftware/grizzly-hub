import { AuthEffects } from './authentication/auth.effects';
import { DashboardEffects } from './dashboard/dashboard.effect';
import { MicroserviceEffects } from './microservice/microservice.effect';
import { NotificationEffect } from './notification/notification.effect';
import { SwaggerEffects } from './swagger/swagger.effects';
import {SubscriptionEffect} from './subscription/subscription.effect';
import { OrganizationEffects } from './organization/organization.effect';
import { TeamEffects } from './team/team.effect';
export const effects: any[] = [
    AuthEffects,
    DashboardEffects,
    MicroserviceEffects,
    SwaggerEffects,
    NotificationEffect,
    SubscriptionEffect,
    OrganizationEffects,
    TeamEffects
];

