import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { Microservice } from 'src/app/shared/models/Microservice';
import { MicroservicesState } from 'src/app/store/microservice/mircoservice.state';
import { MatDialog } from '@angular/material/dialog';
import { MicroserviceModalComponent } from '../microservice-modal/microservice-modal.component';
import { MicroserviceService } from '../microservice.service';
import { OrganizationService } from 'src/app/organization/organization-menu/organization.service';
import { TeamService } from 'src/app/organization/organization-teams/team.service';
import * as microservice from '../../store/microservice/microservice.actions';
import { DashboardService } from 'src/app/layout/dashboard/dashboard.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-microservice-list',
  templateUrl: './microservice-list.component.html',
  styleUrls: ['./microservice-list.component.scss'],
})
export class MicroserviceListComponent implements OnInit {
  microservice = new Microservice();
  public microservicesList: any[];
  public swaggersList: any[] = [];
  teamMicroservices = [];
  myhubMicroservices = [];

  teamIds: any[] = [];
  constructor(
    private store: Store<MicroservicesState>,
    public dialog: MatDialog,
    private microserviceService: MicroserviceService,
    private organisationService: OrganizationService,
    private teamSevice: TeamService,
    private dashboardService: DashboardService,
    private router: Router
  ) { }

  ngOnInit() {
    console.log(localStorage.getItem('token'))
    if (localStorage.getItem('token')) {
      setTimeout(() => {
        this.organisationService.getCurrentUserTeams().subscribe((teams: any[]) => {
          teams.forEach(element => {
            this.teamIds.push(element.id)
          });
          localStorage.setItem('myTeamsIds', JSON.stringify(this.teamIds));
        })
      }, 1);
      const accessor = 'microservices';
      const personnalAccessor = 'personnal'
      const sharedAccessor = 'shared'
      const publicAcessor = 'publique';
      // tslint:disable-next-line: no-string-literal
      this.store.select('microservice').subscribe((state) => {
        this.microservicesList = state[accessor];
        this.myhubMicroservices = state[personnalAccessor].concat(state[publicAcessor].filter(e => e.owner === localStorage.getItem('userEmail')))
        console.log(this.myhubMicroservices)
        this.myhubMicroservices = [...new Set(this.myhubMicroservices)];
        this.teamMicroservices = state[sharedAccessor];
        this.teamMicroservices.forEach(element => {
          element.teamNames = []
          element.teamIds.forEach(el => {
            this.teamSevice.getTeam(el).subscribe((te: any) => {
              if (!element.teamNames.some(n => n === te.trigramme)) {
                element.teamNames.push(te.trigramme)
              }
            })
          });
        });
      });
    }
    // tslint:disable-next-line: no-string-literal
  }
  load() {
    if (localStorage.getItem('anonymos') === 'true') {
      this.dashboardService.tryToConnect()
    } else {
      this.router.navigateByUrl('app/microservice/myhub')
      this.store.dispatch(
        new microservice.LoadAllMicroservices({})
      );
    }
  }
  loadTeam() {
    if (localStorage.getItem('anonymos') === 'true') {
      this.dashboardService.tryToConnect()
    } else {
      this.router.navigateByUrl('app/team-hub')
      this.store.dispatch(
        new microservice.LoadAllMicroservices({})
      );
    }
  }

  navigateToMicroservice(id) {
    this.router.navigateByUrl('/app/microservice/' + id)
  }
  // Open the modal with the create microservice form
  openDialog(): void {
    if (localStorage.getItem('anonymos') === 'true') {
      this.dashboardService.tryToConnect()
    } else {
      this.dialog.open(MicroserviceModalComponent, {
        // Modal configuration
        width: '1220px',
        height: '90vh',
        position: {
          top: '9vh',
        },
        data: {
          microservice: this.microservice,
          swaggers: this.swaggersList,
          action: {
            update: false,
            create: true,
            msg: 'popups.microservice.add',
          },
        },
      });
    }
  }
}
