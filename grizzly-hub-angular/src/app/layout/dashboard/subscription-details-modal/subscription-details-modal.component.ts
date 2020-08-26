import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Microservice } from '../../../shared/models/Microservice';
import { SubscriptionDto } from '../../../shared/models/Subscription';
import { SubscriptionService } from './subscription.service';
import { Store } from '@ngrx/store';
import { SubscriptionState } from '../../../store/subscription/subscription.state';
import * as subscriptionActions from '../../../store/subscription/subscription.actions';
import { MicroservicesState } from 'src/app/store/microservice/mircoservice.state';


@Component({
  selector: 'app-subscription-details-modal',
  templateUrl: './subscription-details-modal.component.html',
  styleUrls: ['./subscription-details-modal.component.scss']
})
export class SubscriptionDetailsModalComponent implements OnInit {
  microservice: Microservice;
  public selection: string;
  public emailOption: string;
  environments;
  environmentsList = [];
  changesList = [];
  checked;
  subscription;
  userSubcriptions: any[];
  s = 0;
  f = 4;
  list = ['endpoints', 'codes', 'responses', 'params', 'ui modifications'];
  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private dialog: MatDialog, public dialogRef: MatDialogRef<SubscriptionDetailsModalComponent>, private subscriptionService: SubscriptionService, private store: Store<SubscriptionState>, private microserviceStore: Store<MicroservicesState>) {
  }

  ngOnInit() {
    this.microservice = this.data.microservice;
    console.log(this.microservice);
    this.subscription = this.data.subscription;
    const subscriptionAccessor = 'subscriptions';
    this.store.select('subscription').subscribe(list => {
      this.userSubcriptions = list[subscriptionAccessor];
    });
    const arr = [];
    this.microservice.swaggersVersions.forEach(e => {
      arr.push(e.environment);
    })
    const set = new Set(arr);
    this.environments = Array.from(set);
    if (this.subscription !== null) {
      this.environments.forEach(en => {
        if (this.subscription.environment.includes(en)) {
          this.environmentsList.push({ name: en, checked: true })
          this.s++;
        } else {
          this.environmentsList.push({ name: en, checked: false })
        }
      })
      this.list.forEach(el => {
        if (this.subscription.changes.includes(el)) {
          this.changesList.push({ name: el, checked: true })
          this.f++;
        } else {
          this.changesList.push({ name: el, checked: false })
        }
      })
      this.selection = this.subscription.frequence.toString();
      if(this.subscription.emailOption != null) {
        this.emailOption = this.subscription.emailOption.toString();
      } else {
        this.emailOption = 'false';
      }

    } else {
      this.environments.forEach(env => {
        this.environmentsList.push({ name: env, checked: false });
      });
      this.list.forEach(e => {
        this.changesList.push({ name: e, checked: true });
      })
    }
  }
  onClick() {
    console.log(this.emailOption)
    const subscibedAccessor = 'subscribed';
    const checkedBoxes = [];
    const changesChecked = [];
    this.environmentsList.forEach(elem => {
      if (elem.checked === true) {
        checkedBoxes.push(elem.name);
      }
    });
    this.changesList.forEach(el => {
      if (el.checked === true) {
        changesChecked.push(el.name);
      }
    });
    const subscription = {
      environment: checkedBoxes,
      frequence: +this.selection,
      microserviceId: this.microservice.id,
      changes: changesChecked,
      userEmail: localStorage.getItem('userEmail'),
      emailOption : this.emailOption
    }
    console.log(subscription)

    this.store.dispatch(new subscriptionActions.Subscribe(subscription));
    this.microserviceStore.select('microservice').subscribe((data: any[]) => {
      if (!data[subscibedAccessor].some(m => m.id === this.microservice.id)) {
        data[subscibedAccessor].push(this.microservice);
      }
    });
    this.dialogRef.close();
  }

  update(event) {
    if (event.checked === true) {
      this.s++;
    } else {
      this.s--;
    }
  }

  updateChanges(event) {
    if (event.checked === true) {
      this.f++;
    } else {
      this.f--;
    }
  }





}

