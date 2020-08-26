import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { MatChipInputEvent } from '@angular/material/chips';
import { COMMA, ENTER } from '@angular/cdk/keycodes'
import { MessageService } from 'src/app/shared/message-modal/message.service';
import { Store } from '@ngrx/store';
import { OrganizationsState } from 'src/app/store/organization/organization.state';
import { OrganizationService } from '../../organization-menu/organization.service';
import { ToastrService } from 'ngx-toastr';
import { AppTranslateService } from 'src/app/shared/services/app-translate-service';
import { TeamService } from '../team.service';
import { ActivatedRoute } from '@angular/router';
import { TeamModalComponent } from '../team-modal/team-modal.component';
import { MatDialog } from '@angular/material/dialog';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { startWith, map } from 'rxjs/operators';
import { TeamsState } from 'src/app/store/team/team.state';

@Component({
  selector: 'app-team-details',
  templateUrl: './team-details.component.html',
  styleUrls: ['./team-details.component.scss']
})
export class TeamDetailsComponent implements OnInit {


  myControl = new FormControl();
  emails: string[] = [];
  filteredOptions: Observable<string[]>;
  @ViewChild('memberInput') memberInput: ElementRef<HTMLInputElement>;
  organization: any;
  organizationNameAbrev: string;
  organizationMembers: any[] = [];
  team: any = {};
  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;
  readonly separatorKeysCodes: number[] = [ENTER, COMMA];
  members: any[] = [];
  teamMembers: any[] = [];

  constructor(private store: Store<OrganizationsState>,
    private teamService: TeamService,
    public activatedRoute: ActivatedRoute,
    private messageBoxService: MessageService,
    public organizationService: OrganizationService,
    public dialog: MatDialog,
    private orgService: OrganizationService,
    private teamStore: Store<TeamsState>) { }

  ngOnInit(): void {
    console.log(this.team)
    const accessor = 'organization'
    this.store.select(accessor).subscribe(state => {
      if (state[accessor].length !== 0) {
        this.organization = state[accessor][0];
        this.organizationNameAbrev = (this.organization.name).substr(0, 2).toUpperCase();
        this.activatedRoute.params.subscribe(params => {
          this.teamService.getMembersByTeam(params.id).subscribe((res: any[]) => {
            this.teamMembers = res;
          })
          const teamAccessor = 'team';
          this.teamStore.select(teamAccessor).subscribe(teamState => {
            this.team = teamState[teamAccessor][0];
          })
          this.teamService.getTeam(params.id).subscribe(team => {
            this.team = team;
          })
        })

      }
    })
    this.orgService.getMembersOrganizationById(this.organization?.id).subscribe(members => {
      members.forEach(member => {
        this.emails.push(member.email);
      });
    })

    this.filteredOptions = this.myControl.valueChanges
    .pipe(
      startWith(''),
      map(value => this._filter(value))
    );
  }
  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.emails.filter(option => option.toLowerCase().includes(filterValue));
  }

  invite() {
    this.members.forEach(member => {
      this.organizationService.checkMemberIsUser(member.name).subscribe(isUser => {
        if (isUser) {
          this.organizationService.checkMemberInCurrentOrganization(this.organization.id, member.name).subscribe(res => {
            if (res) {
              this.teamService.addMemberToTeam(this.team.id, member.name).subscribe((newMember : any) => {
                this.team.totalMembers++;
                this.teamService.updateTeam(this.team, this.team.id).subscribe(result => {
                  if(!this.teamMembers.some(el => el.email === newMember.email))
                    this.teamMembers.push(newMember);
                  this.members.length = 0;
                })
              })
            }
          })
        }
      })
    })
  }
  deleteMemberFromTeam(email) {
    this.messageBoxService.openWarning('popups.member.delete.title', 'popups.member.delete.msg',
      {
        info: {
          msg: 'messageBox.member.delete',

        }
      }).afterClosed().subscribe((data) => {
        if (data) {
          this.teamService.deleteMemberFromTeam(this.team.id, email).subscribe(res => {
            this.team.totalMembers--;
            this.teamService.updateTeam(this.team, this.team.id).subscribe(result => {
              const index = this.teamMembers.findIndex(t => t.email === email);
              this.teamMembers.splice(index, 1);
              this.members.length = 0;
            })


          })
        }
      });

  }
  editTeam() {
    this.dialog.open(TeamModalComponent,
      { // Modal configuration
        width: '70%',
        height: '90vh',
        position: {
          top: '9vh'
        },
        data: {
          team: this.team,
          organization : this.organization,
          action: {
            update: true,
            create: false,
            msg: 'popups.team.edit'
          }
        },
      });
  }



  add(event: MatChipInputEvent): void {

    const input = event.input;
    const value = event.value;

    // Add our member
    if ((value || '').trim()) {
      this.members.push({ name: value.trim() });
    }

    // Reset the input value
    if (input) {
      input.value = '';
    }
  }

  addOption(option) {

    if ((option || '').trim() && (!this.members.some(member => member.name === option))) {
      this.members.push({ name: option.trim() });
      this.memberInput.nativeElement.value = '';
    }
    else{
      this.memberInput.nativeElement.value = '';
    }
  }

  remove(fruit: any): void {
    const index = this.members.indexOf(fruit);

    if (index >= 0) {
      this.members.splice(index, 1);
    }
  }
}
