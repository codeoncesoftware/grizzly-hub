import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NotificationState } from '../../store/notification/notification.state';
import { Store } from '@ngrx/store';
import * as notifAction from '../../store/notification/notification.actions'
import { MessageService } from '../../shared/message-modal/message.service';
import { DiffContentModalService } from '../../swagger/swagger-diff-modal/swagger-diff-content/diff-content-modal.service';

@Component({
  selector: 'app-notification-list',
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.sass']
})
export class NotificationListComponent implements OnInit {

  notifications = [];
  constructor(private http: HttpClient, private store: Store<NotificationState>, private messageBoxService: MessageService, private diffService: DiffContentModalService) { }

  ngOnInit(): void {
    this.store.dispatch(new notifAction.LoadAllNotifications({}));
    const notifications = 'notifications'
    this.store.select('notification').subscribe(res => {
      this.notifications = res[notifications];
    })
  }

  public openConfirmDeleteDialog(notif) {
    this.messageBoxService.openWarning('Delete Notification', 'popups.microservice.delete.msg',
      {
        title: 'this notification',
      }).afterClosed().subscribe((data) => {
        if (data) {
          this.store.dispatch(new notifAction.DeleteNotification(notif.id));
        }
      });

  }

  openDiffModal(oldSwagger, newSwagger) {
    this.diffService.openDiffModal(oldSwagger, newSwagger, 'notification');
  }

}
