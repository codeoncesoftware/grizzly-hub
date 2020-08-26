import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AuthService } from 'src/app/auth/auth.service';
import { Store } from '@ngrx/store';
import { SubscriptionState } from 'src/app/store/subscription/subscription.state';
import * as subscriptionActions from '../../../store/subscription/subscription.actions';
import { SubscriptionDetailsModalComponent } from '../../dashboard/subscription-details-modal/subscription-details-modal.component';
import { OrganizationService } from 'src/app/organization/organization-menu/organization.service';
import { TeamService } from 'src/app/organization/organization-teams/team.service';
import { MicroservicesState } from 'src/app/store/microservice/mircoservice.state';
import * as microserviceActions from '../../../store/microservice/microservice.actions';
import { MicroserviceService } from 'src/app/microservice/microservice.service';
import { SubscriptionService } from '../../dashboard/subscription-details-modal/subscription.service';

@Component({
  selector: 'app-share-microservice',
  templateUrl: './share-microservice.component.html',
  styleUrls: ['./share-microservice.component.scss']
})
export class ShareMicroserviceComponent implements OnInit {

  public usersToAdd = [];
  public teams = [];
  showInput = false;
  showNoUsers = false;
  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
    private teamService: TeamService,
    private store: Store<SubscriptionState>,
    private microserviceStore: Store<MicroservicesState>,
    public dialogRef: MatDialogRef<SubscriptionDetailsModalComponent>,
    private microserviceService: MicroserviceService,
    private organisationService: OrganizationService,
    private subscriptionService: SubscriptionService) {

      if (this.data.microservice.shared) {
        this.showInput = false;
        this.organisationService.getMyOrganizationTeams().subscribe((teams: any[]) => {
          this.teams = teams;
          this.data.microservice.teamIds.forEach(element => {
            const index = this.teams.findIndex(t => t.id === element);
            if (index >= 0) {
              this.teamService.getMembersByTeam(element).subscribe((members: any[]) => {
                members.forEach(el => {
                  this.subscriptionService.isSubscribed(el.email, this.data.microservice.id).subscribe(res => {
                    this.showNoUsers = true;
                    if (!this.usersToAdd.some(u => u.email === el.email) && !res) {
                      this.usersToAdd.push({ email: el.email, team: this.teams[index].name })
                    }
                  })
                });
              })
            }
          });
        })
      } else {
        this.showInput = true;
        this.organisationService.getMyOrganizationTeams().subscribe((teams: any[]) => {
          this.teams = teams;
        })
      }
     }
  ngOnInit() {





  }

  selectAllUsersOfTeams() {
    this.teams.forEach(team => {
      this.teamService.getMembersByTeam(team.id).subscribe((members: any[]) => {
        members.forEach(element => {
          console.log(element.email);
          console.log(team)
          if(element.email !== team.owner) {
            if (!this.usersToAdd.some(u => u.email === element.email)) {
              this.usersToAdd.push({ email: element.email, team: team.name })
            }
          }
        });
      })
    })
  }

  addUsers(team) {
    const index = this.teams.findIndex(t => t.name === team);
    if (index >= 0) {
      const teamId = this.teams[index].id;
      this.teamService.getMembersByTeam(teamId).subscribe((members: any[]) => {
        members.forEach(element => {
          const teamOwner = this.teams[index].owner;
          if(element.email !== teamOwner) {
          if (!this.usersToAdd.some(u => u.email === element.email)) {
            this.usersToAdd.push({ email: element.email, team })
          }
        }
        });
      })
    }


  }

  deleteUserRow(index) {
    this.usersToAdd.splice(index, 1);
  }

  confirm() {
    const microservice = Object.assign({}, this.data.microservice);
    microservice.type = 'shared';
    this.usersToAdd.forEach(u => {
      const index = this.teams.findIndex(t => t.name === u.team);
      if (index >= 0) {
        const teamId = this.teams[index].id;
        if (!microservice.teamIds.some(us => us === teamId)) {
          microservice.teamIds.push(teamId);
        }
      }
    })
    delete microservice.swaggersVersions;
    const newForm = new FormData();

    this.microserviceService.getSwaggersByMicroserviceId(microservice.id).subscribe((data: any[]) => {

      const obj = { microservice, swaggers: data };
      newForm.append(
        'data',
        new Blob([JSON.stringify(obj)], { type: 'application/json' })
      );
      console.log(obj);
      this.microserviceStore.dispatch(
        new microserviceActions.UpdateMicroserviceAttachFileRequest(newForm)
      );

      const subscription = Object.assign({}, this.data.subscription)
      this.usersToAdd.forEach(el => {
        subscription.userEmail = el.email
        this.store.dispatch(new subscriptionActions.Subscribe(subscription));
      })
      const personnalAcessor = 'personnal';
      const sharedAcessor = 'shared'
      const sub = this.microserviceStore.select('microservice').subscribe(res => {
        const index = res[personnalAcessor].findIndex(r => r.id === microservice.id);
        if (index >= 0) {
          res[sharedAcessor].push(res[personnalAcessor][index]);
          res[personnalAcessor].splice(index, 1);
        }
        if (sub)
          sub.unsubscribe();
      })
    })

  }
}
