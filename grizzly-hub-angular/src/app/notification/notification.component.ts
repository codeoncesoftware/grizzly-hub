import { Component, Input, OnInit } from '@angular/core';
import { Notification } from '../shared/models/Notification';
import { DiffContentModalService } from '../swagger/swagger-diff-modal/swagger-diff-content/diff-content-modal.service';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { MicroservicesState } from '../store/microservice/mircoservice.state';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.sass']
})
export class NotificationComponent implements OnInit {
  @Input() notification: any;
  private microservicesList = [];
  private mode = 'notification'
  constructor(private diffService: DiffContentModalService, private router: Router, private store: Store<MicroservicesState>,
  ) { }

  ngOnInit(): void {
    const accessor = 'microservices'
    this.store.select('microservice').subscribe((state) => {
      this.microservicesList = state[accessor];
    });
    if (this.notification.commentaire) {
      if (this.notification.commentaire !== 'new' && this.notification.commentaire !== 'missing' && this.notification.commentaire !== 'params') {
        if (localStorage.getItem('grizzly-lang') === 'fr' && !( this.notification.commentaire.includes(' a été modifié ') || this.notification.commentaire.includes(' ont été modifiés '))) {
          if (this.notification.commentaire.split(' ').length - 1 === 2 ) {
            this.notification.commentaire += ' a été modifié '
          } else {
            this.notification.commentaire += ' ont été modifiés '
          }
        }
        if (localStorage.getItem('grizzly-lang') === 'en' && !( this.notification.commentaire.includes(' has been changed ') || this.notification.commentaire.includes(' have been changed '))) {
          console.log(this.notification.commentaire)
          if (this.notification.commentaire.split(' ').length - 1 === 2   ) {

            this.notification.commentaire += ' has been changed '
          } else {
            this.notification.commentaire += ' have been changed '
          }
          console.log(this.notification.commentaire)
        }
      }
    }
  }

  openDiffModal(notification) {
    if (notification.idOldSwagger === null) {
      const micro = this.microservicesList.filter(microservice => microservice.title === notification.microserviceTitle)[0];
      this.router.navigate(['/app/microservice/', micro?.id]);
    } else {
      this.diffService.openDiffModal(notification.idOldSwagger, notification.idNewSwagger, this.mode);
    }
  }
}
