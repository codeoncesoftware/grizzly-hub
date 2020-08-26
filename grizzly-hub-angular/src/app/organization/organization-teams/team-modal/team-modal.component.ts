import { Component, OnInit, Inject } from '@angular/core';
import { Team } from 'src/app/shared/models/Team';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { TeamService } from '../team.service';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AppTranslateService } from 'src/app/shared/services/app-translate-service';
import * as organizationActions from '../../../store/organization/organization.actions';
import { Store } from '@ngrx/store';
import { OrganizationsState } from 'src/app/store/organization/organization.state';

import * as teamActions from '../../../store/team/team.actions';
import { TeamsState } from 'src/app/store/team/team.state';
@Component({
  selector: 'app-team-modal',
  templateUrl: './team-modal.component.html',
  styleUrls: ['./team-modal.component.sass']
})
export class TeamModalComponent implements OnInit {

  team: any = {};
  organizationId: string;
  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
    private store: Store<OrganizationsState>,
    private teamStore: Store<TeamsState>,
    public activatedRoute: ActivatedRoute,
    public dialogRef: MatDialogRef<TeamModalComponent>,
    public dialog: MatDialog,
    private teamService: TeamService,
    private toaster: ToastrService,
    private translateService: AppTranslateService,) { }

  ngOnInit(): void {
    this.team = { ...this.data.team };
    console.log(this.data);
    // const teamAccessor = 'team';
    // this.teamStore.select(teamAccessor).subscribe(state => {
    //   console.log(state)
    //   this.team = state[teamAccessor][0];
    // })

  }
  generateTrigramme(name) {
    const res = name.split(' ');
    let a = '';
    res.forEach(element => {
      if (element !== '' && a.length < 3)
        a += element[0].toUpperCase();
    });
    this.team.trigramme = a;
  }

  Save() {
    const teamObj = {
      ...this.team,
      organisationId: this.data.organization.id
    }
    if (this.data.action.create) {
      this.teamService.addTeam(teamObj).subscribe(team => {
        this.data.organization.totalTeams++;
        this.store.dispatch(new organizationActions.UpdateOrganization({ organization: this.data.organization, id: this.data.organization.id }));
        this.data.teams.push(team);
        this.dialogRef.close();
        this.toaster.success(this.translateService.getMessage('toaster.team.added'));
      })
    }
    if (this.data.action.update) {
      // call update team action
      this.teamStore.dispatch(new teamActions.UpdateTeam({team : teamObj, id: this.team.id}));
      // this.teamService.updateTeam(teamObj, this.team.id).subscribe(team => {
      //   this.data.team = team;
      //   this.teamService.getTeam(teamObj.id).subscribe()
      this.dialogRef.close();
      //   this.toaster.success(this.translateService.getMessage('toaster.team.updated'));
      // })
    }

  }

}
