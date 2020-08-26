import {
  Component,
  OnInit,
  ChangeDetectorRef,
  ViewChild,
  OnDestroy,
} from '@angular/core';
import { DashboardService } from './dashboard.service';
import { Store } from '@ngrx/store';

import { Swagger } from 'src/app/shared/models/Swagger';
import { Microservice } from 'src/app/shared/models/Microservice';
import { MicroservicesState } from 'src/app/store/microservice/mircoservice.state';
import { MatDialog } from '@angular/material/dialog';
import { SubscriptionDetailsModalComponent } from './subscription-details-modal/subscription-details-modal.component';
import { SubscriptionState } from 'src/app/store/subscription/subscription.state';
import * as subscription from '../../store/subscription/subscription.actions';
import { MessageService } from 'src/app/shared/message-modal/message.service';

import { SubscriptionService } from './subscription-details-modal/subscription.service';

import { SubscriptionDto } from '../../shared/models/Subscription';
import { MicroserviceService } from 'src/app/microservice/microservice.service';
import * as microserviceAction from '../../store/microservice/microservice.actions';
import { Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { SubscribeModalComponent } from './subscribe-modal/subscribe-modal.component';
import { Router } from '@angular/router';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit, OnDestroy {
  public swaggers: Swagger[];
  public microservices: Microservice[];
  public content: any[];
  public loading = true;
  public ms: string;
  public filteredMicroservices: Microservice[];
  public microserviceNames: string[];

  public subscribed: any[];
  public userSubcriptions: any[];
  subscription: SubscriptionDto;
  page = 0;
  storeSubscription: Subscription;
  scrollDisabled = false;

  constructor(
    private store: Store<MicroservicesState>,
    private secondStore: Store<SubscriptionState>,
    private dashboardService: DashboardService,
    private dialog: MatDialog,
    private messageBoxService: MessageService,
    private subscriptionService: SubscriptionService,
    private microserviceService: MicroserviceService,
    private router: Router
  ) { }

  ngOnInit() {
    if (localStorage.getItem('token')) {
      this.subscribed = [];
      const publicAccessor = 'publique';
      const privateAccessor = 'personnal';
      const sharedAccessor = 'shared';
      const subscriptionAccessor = 'subscriptions';
      const fetch = 'success';
      this.storeSubscription = this.store
        .select('microservice')
        .subscribe((state) => {
          // console.log(state)
          this.microservices = state[publicAccessor].concat(state[privateAccessor]).concat(state[sharedAccessor]);
          this.filteredMicroservices = this.microservices;
          this.loading = !state[fetch];
          this.secondStore.select('subscription').subscribe((secondState) => {
            this.userSubcriptions = secondState[subscriptionAccessor];
            this.subscribed.length = 0;
            if (this.microservices) {
              this.microservices.forEach((element) => {
                if (
                  this.userSubcriptions.some((u) => u.microserviceId === element.id)
                ) {
                  this.subscribed.push(true);
                } else {
                  this.subscribed.push(false);
                }
              });
            };

          });
        });


    }
    else {
      this.microserviceService.getAllPublicMicroservices(10, 0).subscribe(res => {
        this.loading = false
        console.log(res)
        this.filteredMicroservices = res
        this.subscribed = [];
        this.filteredMicroservices.forEach(el => {
          this.subscribed.push(false);
        })
        console.log(this.filteredMicroservices)
        localStorage.setItem('anonymos', 'true')
      })
    }
  }

  ngOnDestroy(): void {
    if (localStorage.getItem('token')) {
      this.storeSubscription.unsubscribe();
    }
  }



  public onSearchInputChange(term: string): void {
    console.log(term)
    this.filteredMicroservices = this._filter(term);
  }

  private _filter(value: string): Microservice[] {
    const filterValue = value.toLowerCase();
    if (localStorage.getItem('token')) {
      return this.microservices.filter(option => option.title.toLowerCase().includes(filterValue));
    } else {
      return this.filteredMicroservices.filter(option => option.title.toLowerCase().includes(filterValue));
    }

  }

  onScrollDown() {
    if (!this.scrollDisabled) {
      if (localStorage.getItem('token')) {
        this.page = this.page + 1;
        this.microserviceService
          .getAllMicroservices(10, this.page)
          .subscribe((res) => {
            console.log(res);
            // this.filteredMicroservices = this.filteredMicroservices.concat(
            //   res.content.filter(ms => ms.publique === true)
            // );
            this.microservices = this.microservices.concat(res.content);
            if (this.microservices.length === res.totalElements) {
              this.scrollDisabled = true;
            }
            this.store.dispatch(
              new microserviceAction.ChangeMicroservice(this.microservices)
            );
            this.subscribed.length = 0;
            this.microservices.forEach((element) => {
              this.userSubcriptions.some((u) => u.microserviceId === element.id) ? this.subscribed.push(true) : this.subscribed.push(false);;
            });
          });
      }
      else {
        this.page = this.page + 1;
        this.microserviceService.getAllPublicMicroservices(10, this.page).subscribe(res => {
          if (this.filteredMicroservices.length === res.totalElements) {
            this.scrollDisabled = true;
          }
          this.filteredMicroservices.concat(res)
        });
      }
    }
    
  }
  openMicroservice(microservice) {
    if (localStorage.getItem('token')) {
      this.router.navigateByUrl('/app/microservice/' + microservice.id)
    } else {
      this.router.navigateByUrl('/app/microservice/' + microservice.title.split(' ').join('_'))
    }
  }

  async getSubscription(ms) {
    return await this.subscriptionService.getSubscription(ms.id).toPromise();
  }

  async goToModal(ms) {
    if (!localStorage.getItem('token')) {
      this.dashboardService.tryToConnect()
    } else {
      const sub = await this.getSubscription(ms);
      this.openModal(ms, sub);
    }
  }


  openModal(ms, sub): void {
    this.dialog.open(SubscriptionDetailsModalComponent, {
      width: '1000px',
      height: '80vh',
      position: {
        top: '10vh',
        right: '20vh',
      },
      data: {
        microservice: ms,
        subscription: sub,
      },
    });
  }
  // Open the modal to get confirmation for delete
  public openConfirmDeleteDialog(id) {
    const subscibedAccessor = 'subscribed';
    const microserviceAccessor = 'microservice';
    const subscriptionId = this.userSubcriptions.filter(
      (subs) => subs.microserviceId === id
    )[0].id;
    this.messageBoxService
      .openWarning(
        'popups.subscription.delete.title',
        'popups.subscription.delete.msg',
        {
          title: 'messageBox.subscription.deleteTitle',
          info: {
            msg: 'messageBox.subscription.delete',
            infos: ['messageBox.subscription.noNotifications'],
          },
        }
      )
      .afterClosed()
      .subscribe((data) => {
        if (data) {
          this.store.dispatch(
            new subscription.DeleteSubscription(subscriptionId)
          );
          this.store.select(microserviceAccessor).subscribe((resp: any[]) => {
            const subscribedMicroservices: any[] = resp[subscibedAccessor];
            const index = subscribedMicroservices.findIndex(m => m.id === id);
            subscribedMicroservices.splice(index, 1);
          })
        }
      });
  }
}
