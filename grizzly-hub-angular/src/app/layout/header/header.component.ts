import { Component, OnInit } from '@angular/core';
import { APPCONFIG } from '../../config';
import * as authActions from './../../store/authentication/auth.actions';
import { AppTranslateService } from 'src/app/shared/services/app-translate-service';
import { AuthService } from 'src/app/auth/auth.service';
import { Store } from '@ngrx/store';
import { AuthState } from 'src/app/store/authentication/auth.state';
import { WebSocketService } from '../../shared/web-socket.service';
import { NotificationState } from '../../store/notification/notification.state';
import { Notification } from '../../shared/models/Notification';
import { NotificationService } from '../../notification/notification.service';
import * as notifActions from '../../store/notification/notification.actions';
import { DashboardService } from '../dashboard/dashboard.service';

@Component({
  selector: 'app-header',
  styleUrls: ['./header.component.scss'],
  templateUrl: './header.component.html',
})
export class AppHeaderComponent implements OnInit {
  public AppConfig: any;
  isFr: boolean;
  selectedLanguage: string;

  firstName: string;
  lastName: string;
  notificationNumber = 0;
  public notifications: Notification[];
  matBadgeHidden: boolean;
  public newNotifications: Notification[];
  public oldNotifications: Notification[];
  public oldTopNotifications: Notification[];
  hidden: boolean;
  pageOptions = '&pageSize=1000&page=0';
  anonymos = !localStorage.getItem('token')

  constructor(
    private appTranslateService: AppTranslateService,
    private authService: AuthService,
    private store: Store<AuthState>,
    private webSocketService: WebSocketService,
    private notifStore: Store<NotificationState>,
    private notificationService: NotificationService,
    private dashboardService : DashboardService
  ) {
    const stompClient = this.webSocketService.connect();
    stompClient.connect({}, (frame) => {
      console.log('aa');
      // Subscribe to notification topic
      stompClient.subscribe(
        '/topic/notification?email=' +
        localStorage.getItem('userEmail'),
        (notifications) => {
          // Update notifications attribute with the recent messsage sent from the server
          this.notificationNumber++;
          const notif = JSON.parse(notifications.body);
          this.notifStore.dispatch(new notifActions.AddNotifications(notif));
          console.log(notif);
        }
      );
    });
  }

  ngOnInit() {
    this.AppConfig = APPCONFIG;
    // Set i18n Language
    if (localStorage.getItem('grizzly-lang')) {
      this.setLang(localStorage.getItem('grizzly-lang').toLowerCase());
    } else {
      this.setLang(navigator.language);
    }

    this.store.select('auth').subscribe((res) => {
      if (res.user.firstName) {
        this.firstName = res.user.firstName;
      } else {
        this.store.dispatch(
          new authActions.LoginUser(localStorage.getItem('userEmail'))
        );
      }
    });

    const notifications = 'notifications';
    this.notifStore.select('notification').subscribe((res) => {
      this.notifications = res[notifications];
      console.log(this.notifications);
      this.newNotifications = [];
      this.oldNotifications = [];
      this.oldTopNotifications = [];
      if (this.notifications.length > 0) {
        this.notifications.forEach((notif) => {
          if (notif.opened === false) {
            this.newNotifications.push(notif);
          } else {
            this.oldNotifications.push(notif);
          }
        });
      }
      this.oldTopNotifications = this.oldNotifications.slice(0, 6);
      this.notificationNumber = this.newNotifications.length;
    });
  }

  setLang(lang: string) {
    this.appTranslateService.setDefaultLang(lang);
    localStorage.setItem('grizzly-lang', lang);

    if (lang.includes('fr')) {
      this.selectedLanguage = 'FR';
      this.isFr = false;
    } else {
      this.selectedLanguage = 'EN';
      this.isFr = true;
    }
  }

  logout() {
    this.authService.logout();
  }

  readNotif() {
    if (localStorage.getItem('grizzly-lang')) {
    setTimeout(() => {
      this.oldNotifications = [
        ...this.oldNotifications,
        ...this.newNotifications,
      ];
      this.notifStore.dispatch(new notifActions.SeeNotifications());
      this.newNotifications.length = 0;
    }, 1000);
  }
  }

  clearAll() {
    if (localStorage.getItem('grizzly-lang')) {
    this.notifStore.dispatch(new notifActions.DeleteNotifications());
    this.newNotifications.length = 0;
    this.oldNotifications.length = 0;
    this.oldTopNotifications.length = 0;
    this.hidden = true;
  }
}

  openNotif() {
    if (this.anonymos) {
      this.dashboardService.tryToConnect()
    }
    else this.notificationNumber = 0;
  }
}

