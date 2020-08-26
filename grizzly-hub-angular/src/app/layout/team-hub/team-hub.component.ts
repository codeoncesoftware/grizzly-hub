import { Component, OnInit } from '@angular/core';
import { Microservice } from '../../shared/models/Microservice';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { MicroserviceService } from '../../microservice/microservice.service';
import { SubscriptionDetailsModalComponent } from '../dashboard/subscription-details-modal/subscription-details-modal.component';
import { ShareMicroserviceComponent } from '../my-hub/share-microservice/share-microservice.component';
import { Store } from '@ngrx/store';
import { MicroservicesState } from '../../store/microservice/mircoservice.state';
import { SubscriptionState } from '../../store/subscription/subscription.state';
import { DashboardService } from '../dashboard/dashboard.service';
import { MatDialog } from '@angular/material/dialog';
import { MessageService } from '../../shared/message-modal/message.service';
import { SubscriptionService } from '../dashboard/subscription-details-modal/subscription.service';
import * as subscription from '../../store/subscription/subscription.actions'
import * as microservice from '../../store/microservice/microservice.actions';
import { Swagger } from '../../shared/models/Swagger';
import { SubscriptionDto } from '../../shared/models/Subscription';

@Component({
  selector: 'app-team-hub',
  templateUrl: './team-hub.component.html',
  styleUrls: ['./team-hub.component.scss']
})
export class TeamHubComponent implements OnInit {

  public swaggers: Swagger[];
  public microservices = [];
  public content: any[];
  public loading = true;
  public filter: string;
  public filteredMicroservices: Microservice[];
  public subscribed: any[];
  public userSubcriptions: any[];
  subscription: SubscriptionDto;
  public microserviceList: Microservice[];
  constructor(private http: HttpClient,
    private microserviceService: MicroserviceService,
    private store: Store<MicroservicesState>,
    private secondStore: Store<SubscriptionState>,
    private dialog: MatDialog,
    private messageBoxService: MessageService,
    private subscriptionService: SubscriptionService) { }

  ngOnInit(): void {
        if (localStorage.getItem('token') ) {

    this.subscribed = [];
    const accessor = 'shared';
    const subscriptionAccessor = 'subscriptions';
    this.secondStore.select('subscription').subscribe(secondState => {
      this.store.select('microservice').subscribe(state => {
        if (state) {
          this.microservices = [...state[accessor]];
          this.microserviceList = this.microservices
          this.userSubcriptions = secondState[subscriptionAccessor];
          if (this.microservices.length > 0) {
            this.subscribed.length = 0;
            this.microservices.forEach(element => {
              if (this.userSubcriptions.some(u => u.microserviceId === element.id)) {
                this.subscribed.push(true);
              } else {
                this.subscribed.push(false);
              }
            });
          }
        }
      });
    })
  }
  }

  async getSubscription(ms) {
    return await this.subscriptionService.getSubscription(ms.id).toPromise();
  }

  async goToModal(ms) {
    const sub = await this.getSubscription(ms);
    this.openModal(ms, sub);
  }
  openModal(ms, sub): void {
    this.dialog.open(SubscriptionDetailsModalComponent, {
      width: '1000px',
      height: '80vh',
      position: {
        top: '10vh',
        right: '20vh'
      },
      data: {
        microservice: ms,
        subscription: sub
      },
    })
  }
  async openShareModal(ms) {
    const sub = await this.getSubscription(ms);
    this.dialog.open(ShareMicroserviceComponent, {
      width: '700px',
      height: '80vh',
      position: {
        top: '10vh',
        right: '50vh'
      },
      data: {
        microservice: ms,
        subscription: sub
      },
    })
  }
  // Open the modal to get confirmation for delete
  public openConfirmDeleteDialog(id) {
    const subscriptionId = this.userSubcriptions.filter(subs => subs.microserviceId === id)[0].id;
    this.messageBoxService.openWarning('popups.subscription.delete.title', 'popups.subscription.delete.msg',
      {
        title: 'messageBox.subscription.deleteTitle',
        info: {
          msg: 'messageBox.subscription.delete',
          infos: ['messageBox.subscription.noNotifications']
        }
      }).afterClosed().subscribe((data) => {
        if (data) {
          this.store.dispatch(new subscription.DeleteSubscription(subscriptionId));

        }
      });

  }

}
