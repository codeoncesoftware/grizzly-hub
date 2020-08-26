import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { OrganizationModalComponent } from '../organization-modal/organization-modal.component';
import { Store } from '@ngrx/store';
import { OrganizationsState } from 'src/app/store/organization/organization.state';
import { OrganizationService } from './organization.service';
import { DashboardService } from 'src/app/layout/dashboard/dashboard.service';

@Component({
  selector: 'app-organization-menu',
  templateUrl: './organization-menu.component.html',
  styleUrls: ['./organization-menu.component.scss'],
})
export class OrganizationMenuComponent implements OnInit {
  constructor(
    public dialog: MatDialog,
    private store: Store<OrganizationsState>,
    private organizationService: OrganizationService,
    private dashboardService : DashboardService
  ) { }

  organizationShow = false;
  organizationName = '';
  organizationId: string;
  userOrganization: any = {};
  organizationNameAbrev: string;
  menuShow = false;
  ngOnInit(): void {
    const accessor = 'organization';
    this.store.select(accessor).subscribe((state) => {
      if (state[accessor].length !== 0) {
        this.organizationService.checkMemberIsAdmin().subscribe(res => {
          if (res === true) {
            this.menuShow = true;
          }
        })
        this.organizationShow = true;
        this.userOrganization = state[accessor][0];
        this.organizationName = this.userOrganization.name;
        this.organizationNameAbrev = this.organizationName
          .substr(0, 2)
          .toUpperCase();
        this.organizationId = this.userOrganization.id;
      } else {
        this.organizationShow = false;
      }
    });
  }

  // Open the modal with the create organization form
  openDialog(): void {
    if (localStorage.getItem('anonymos') === 'true') {
      this.dashboardService.tryToConnect()
    } else {
      this.dialog.open(OrganizationModalComponent, {
        // Modal configuration
        width: '800px',
        height: '85vh',
        position: {
          top: '9vh',
        },
        data: {
          action: {
            update: false,
            create: true,
            msg: 'popups.organization.add',
          },
        },
      });
    }
  }
}
