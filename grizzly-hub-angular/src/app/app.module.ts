import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ErrorHandler, NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatChipsModule } from '@angular/material/chips';
import { MatNativeDateModule, MatRippleModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule, MAT_DIALOG_DEFAULT_OPTIONS } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSliderModule } from '@angular/material/slider';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSortModule } from '@angular/material/sort';
import { MatStepperModule } from '@angular/material/stepper';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTreeModule } from '@angular/material/tree';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { EffectsModule } from '@ngrx/effects';
// STATE
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
// i18n
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { ToastrModule } from 'ngx-toastr';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthModule } from './auth/auth.module';
import { DashboardComponent } from './layout/dashboard/dashboard.component';
import { AppHeaderComponent } from './layout/header/header.component';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { MainLayoutModule } from './layout/main-layout/main-layout.module';
import { NotFoundComponent } from './layout/not-found/not-found.component';
import { AutoCloseMobileNavDirective } from './layout/sidenav/auto-close-mobile-nav.directive';
import { AccordionNavDirective } from './layout/sidenav/sidenav-menu/accordion-nav.directive';
import { AppendSubmenuIconDirective } from './layout/sidenav/sidenav-menu/append-submenu-icon.directive';
import { HighlightActiveItemsDirective } from './layout/sidenav/sidenav-menu/highlight-active-items.directive';
import { AppSidenavMenuComponent } from './layout/sidenav/sidenav-menu/sidenav-menu.component';
import { SidenavComponent } from './layout/sidenav/sidenav.component';
import { ToggleOffcanvasNavDirective } from './layout/sidenav/toggle-offcanvas-nav.directive';
import { ConfirmModalComponent } from './shared/confirm-modal/confirm-modal.component';
import { GlobalErrorHandler } from './shared/handlers/global-error-handler';
import { HttpErrorInterceptor } from './shared/handlers/http-error.interceptor';
import { LoaderInterceptor } from './shared/loader/loader.interceptor';
import { LoaderService } from './shared/loader/loader.service';
import { SharedModule } from './shared/shared.module';
import { effects } from './store';
import { SettingsComponent } from './layout/settings/settings.component';
import { NgxPhoneSelectModule } from 'ngx-phone-select';
import { layoutReducer } from './store/layout/layout.reducer';
import { authReducer } from './store/authentication/auth.reducer';
import { dashboardReducer } from './store/dashboard/dashboard.reducer';
import { MessageModalComponent } from './shared/message-modal/message-modal.component';
import { MicroserviceListComponent } from './microservice/microservice-list/microservice-list.component';
import { microserviceReducer } from './store/microservice/microservice.reducer';
import { swaggerReducer } from './store/swagger/swagger.reducer';
import { MicroserviceModalComponent } from './microservice/microservice-modal/microservice-modal.component';
import { WebSocketService } from './shared/web-socket.service';
import { NotificationComponent } from './notification/notification.component';
import { notificationReducer } from './store/notification/notification.reducer';
import { SwaggerModalService } from './swagger/swagger-modal/swagger-modal.service';
import { SwaggerModalComponent } from './swagger/swagger-modal/swagger-modal.component';
import { SwaggerListComponent } from './swagger/swagger-list/swagger-list.component';
import { SubscriptionDetailsModalComponent } from './layout/dashboard/subscription-details-modal/subscription-details-modal.component';
import { DiffContentModalComponent } from './swagger/swagger-diff-modal/swagger-diff-content/diff-content-modal/diff-content-modal.component';
import { SwaggerDiffComponent } from './swagger/swagger-diff-modal/swagger-diff.component';
import { subscriptionReducer } from './store/subscription/subscription.reducer';
import { MonacoEditorModule } from 'ngx-monaco-editor';
import { NotificationListComponent } from './notification/notification-list/notification-list.component';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { DocumentationModalComponent } from './microservice/documentation-modal/documentation-modal.component';
import { FaqModalComponent } from './microservice/faq-modal/faq-modal.component';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { MyHubComponent } from './layout/my-hub/my-hub.component';
import { ShareMicroserviceComponent } from './layout/my-hub/share-microservice/share-microservice.component';
import { FileUploadModule } from 'ng2-file-upload';
import { DiffTreeModalComponent } from './swagger/swagger-diff-modal/swagger-diff-content/diff-content-modal/diff-tree-modal/diff-tree-modal.component';
import { OrganizationMenuComponent } from './organization/organization-menu/organization-menu.component';
import { OrganizationDetailsComponent } from './organization/organization-details/organization-details.component';
import { OrganizationModalComponent } from './organization/organization-modal/organization-modal.component';
import { TeamModalComponent } from './organization/organization-teams/team-modal/team-modal.component';
import { organizationReducer } from './store/organization/organization.reducer';
import { TeamHubComponent } from './layout/team-hub/team-hub.component';
import { SanitizeHtmlPipe } from './microservice/sanitize-html.pipe';
import { teamReducer } from './store/team/team.reducer';
import { SubscribeModalComponent } from './layout/dashboard/subscribe-modal/subscribe-modal.component';







export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http, '/assets/i18n/', '.json?cb=' + new Date().getTime());
}


@NgModule({
    declarations: [
        AppComponent,
        MainLayoutComponent,
        SidenavComponent,
        AppHeaderComponent,
        AccordionNavDirective,
        AppendSubmenuIconDirective,
        HighlightActiveItemsDirective,
        AppSidenavMenuComponent,
        AutoCloseMobileNavDirective,
        ToggleOffcanvasNavDirective,
        DashboardComponent,
        NotFoundComponent,
        SettingsComponent,
        MicroserviceListComponent,
        MicroserviceModalComponent,
        NotificationComponent,
        SwaggerModalComponent,
        SubscriptionDetailsModalComponent,
        SwaggerListComponent,
        SubscriptionDetailsModalComponent,
        SwaggerDiffComponent,
        DiffContentModalComponent,
        NotificationListComponent,
        DocumentationModalComponent,
        FaqModalComponent,
        MyHubComponent,
        ShareMicroserviceComponent,
        DiffTreeModalComponent,
        OrganizationMenuComponent,
        OrganizationDetailsComponent,
        OrganizationModalComponent,
        TeamModalComponent,
        TeamHubComponent,
        SubscribeModalComponent
    ],
    imports: [
        StoreModule.forRoot({ layout: layoutReducer, auth: authReducer, dashboard: dashboardReducer, microservice: microserviceReducer, notification: notificationReducer, swagger: swaggerReducer, subscription: subscriptionReducer, organization: organizationReducer, team: teamReducer }),
        // Note that you must instrument after importing StoreModule
        StoreDevtoolsModule.instrument({
            maxAge: 5
        }),
        InfiniteScrollModule,
        NgxPhoneSelectModule,
        EffectsModule.forRoot(effects),
        SharedModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MatAutocompleteModule,
        MatButtonModule,
        MatButtonToggleModule,
        MatCardModule,
        MatCheckboxModule,
        MatChipsModule,
        MatDatepickerModule,
        MatDialogModule,
        MatExpansionModule,
        MatGridListModule,
        MatIconModule,
        MatInputModule,
        MatListModule,
        MatMenuModule,
        MatNativeDateModule,
        MatPaginatorModule,
        MatProgressBarModule,
        MatProgressSpinnerModule,
        MatRadioModule,
        MatRippleModule,
        MatSelectModule,
        MatSidenavModule,
        MatSliderModule,
        MatSlideToggleModule,
        MatSnackBarModule,
        MatSortModule,
        MatTableModule,
        MatTabsModule,
        MatToolbarModule,
        MatTooltipModule,
        MatTreeModule,
        MatBadgeModule,
        MatStepperModule,
        AuthModule,
        MainLayoutModule,
        ToastrModule.forRoot(),
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient]
            }
        }),
        MonacoEditorModule.forRoot(),
        CKEditorModule,
        FileUploadModule,
    ],
    providers: [
        SwaggerModalService,
        WebSocketService,
        LoaderService,
        { provide: HTTP_INTERCEPTORS, useClass: LoaderInterceptor, multi: true },
        { provide: ErrorHandler, useClass: GlobalErrorHandler }, {
            provide: HTTP_INTERCEPTORS,
            useClass: HttpErrorInterceptor,
            multi: true
        },
        { provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: { hasBackdrop: true, direction: 'ltr' } }
    ],
    bootstrap: [AppComponent],
    entryComponents: [ConfirmModalComponent, MessageModalComponent, MicroserviceModalComponent, SwaggerModalComponent, SubscriptionDetailsModalComponent, SwaggerDiffComponent, DiffContentModalComponent, DocumentationModalComponent, FaqModalComponent, ShareMicroserviceComponent, DiffTreeModalComponent, OrganizationModalComponent, TeamModalComponent,SubscribeModalComponent]
})
export class AppModule { }
