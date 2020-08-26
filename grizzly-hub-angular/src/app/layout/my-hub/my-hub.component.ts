import { Component, OnInit, ChangeDetectorRef, ViewChild } from '@angular/core';
import { DashboardService } from '../dashboard/dashboard.service';
import { Store } from '@ngrx/store';
import { Swagger } from 'src/app/shared/models/Swagger';
import { Microservice } from 'src/app/shared/models/Microservice';
import { MicroservicesState } from 'src/app/store/microservice/mircoservice.state';
import { MatDialog } from '@angular/material/dialog';
import { SubscriptionDetailsModalComponent } from '../dashboard/subscription-details-modal/subscription-details-modal.component';
import { SubscriptionState } from 'src/app/store/subscription/subscription.state';
import * as subscription from '../../store/subscription/subscription.actions'
import { MessageService } from 'src/app/shared/message-modal/message.service';

import { SubscriptionService } from '../dashboard/subscription-details-modal/subscription.service';
import { SubscriptionDto } from '../../shared/models/Subscription';

import { MicroserviceService } from 'src/app/microservice/microservice.service';
import * as microserviceAction from '../../store/microservice/microservice.actions'
import { ShareMicroserviceComponent } from './share-microservice/share-microservice.component';
import { Organization } from 'src/app/shared/models/Organization';
import { OrganizationsState } from 'src/app/store/organization/organization.state';

@Component({
  selector: 'app-my-hub',
  templateUrl: './my-hub.component.html',
  styleUrls: ['./my-hub.component.scss']
})
export class MyHubComponent implements OnInit {



  public swaggers: Swagger[];
  public microservices = [];
  public content: any[];
  public loading = true;
  public filter: string;
  public filteredMicroservices: Microservice[];
  public subscribed: any[];
  public userSubcriptions: any[];
  subscription: SubscriptionDto;
  disableScroll = true
  shareBtn = true;

  page = 0;


  constructor(private store: Store<MicroservicesState>,
    private secondStore: Store<SubscriptionState>,
    private orgStore: Store<OrganizationsState>,
    private dashboardService: DashboardService,
    private dialog: MatDialog,
    private messageBoxService: MessageService,
    private subscriptionService: SubscriptionService,
    private microserviceService: MicroserviceService
    ) {
  }

  search() {
    this.dashboardService.search(this.filter).subscribe(e => {

      this.filteredMicroservices = e;
      console.log(e)

    })
  }

  onScrollDown() {
    // this.page = this.page + 1;
    // this.microserviceService
    //   .getAllMicroservices(6, this.page)
    //   .subscribe(res => {
    //     console.log(res.content);
    //     this.filteredMicroservices = this.filteredMicroservices.concat(res.content);
    //     this.microservices = this.microservices.concat(res.content);
    //     this.store.dispatch(new microserviceAction.ChangeMicroservice(this.microservices))
    //     this.subscribed.length = 0;
    //     this.microservices.forEach(element => {
    //       if (this.userSubcriptions.some(u => u.microserviceId === element.id)) {
    //         this.subscribed.push(true);
    //       } else {
    //         this.subscribed.push(false);
    //       }
    //     });
    //   });
    console.log(this.subscribed);
    console.log(this.userSubcriptions);
  }

  ngOnInit() {
    if (localStorage.getItem('token') ) {

    this.subscribed = [];
    const accessor = 'personnal';
    const subscriptionAccessor = 'subscriptions';
    const fetch = 'success';
    const publicAcessor = 'publique';

    this.secondStore.select('subscription').subscribe(secondState => {
      if (secondState) {
        this.store.select('microservice').subscribe(state => {
          if (state) {
            this.orgStore.select('organization').subscribe(orgState => {
              const organization = 'organization'
              const orgs = orgState[organization]
              if(orgs[0].totalTeams > 0) {
                this.shareBtn = false;
              }
            });
            this.microservices = [...state[accessor]].concat(state[publicAcessor].filter(e => e.owner === localStorage.getItem('userEmail')))
            this.filteredMicroservices = this.microservices
            this.loading = !state[fetch];
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
      }
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