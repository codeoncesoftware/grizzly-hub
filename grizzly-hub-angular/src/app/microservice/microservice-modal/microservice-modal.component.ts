import { Component, OnInit, Inject, ViewChild, ElementRef, ChangeDetectorRef, AfterViewChecked } from '@angular/core';
import {
  MatDialogRef,
  MAT_DIALOG_DATA,
  MatDialog,
} from '@angular/material/dialog';

import { MicroserviceService } from '../microservice.service';
import { Store } from '@ngrx/store';
import { MicroservicesState } from 'src/app/store/microservice/mircoservice.state';
import * as microservice from '../../store/microservice/microservice.actions';
import { SwaggerDto } from 'src/app/shared/models/SwaggerDto';
import { FileUploader } from 'ng2-file-upload';
import * as _ from 'lodash';
import { ThemePalette } from '@angular/material/core';
import { SubscriptionState } from 'src/app/store/subscription/subscription.state';
import { MatChipInputEvent } from '@angular/material/chips';
import { ENTER, COMMA } from '@angular/cdk/keycodes';
import { Observable } from 'rxjs';
import { FormControl } from '@angular/forms';
import { map, startWith } from 'rxjs/operators';
import { OrganizationService } from 'src/app/organization/organization-menu/organization.service';
import { TeamService } from 'src/app/organization/organization-teams/team.service';
import { SubscriptionService } from 'src/app/layout/dashboard/subscription-details-modal/subscription.service';

@Component({
  selector: 'app-microservice-modal',
  templateUrl: './microservice-modal.component.html',
  styleUrls: ['./microservice-modal.component.scss'],
})
export class MicroserviceModalComponent implements OnInit, AfterViewChecked {
  color: ThemePalette = 'accent';
  swagger = new SwaggerDto();
  microservice = { title: '', description: '', type: 'private', teamIds: [], owner: '' };
  swaggers: any[] = [];
  invalidSwaggerUrl: boolean;
  public uploader: FileUploader = new FileUploader({});
  fileName = '';
  submitForm = new FormData();
  urlType = 'URL';
  fileType = 'FILE';
  isChecked = false;
  existingName = false;
  containsSwagger = false;
  filteredOptions: Observable<string[]>;
  myControl = new FormControl();
  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;
  readonly separatorKeysCodes: number[] = [ENTER, COMMA];
  teamNames: any[] = [];
  teams: any[] = [];
  @ViewChild('teamInput') teamInput: ElementRef<HTMLInputElement>;
  showTeams = false;
  sharedError = false;
  update = false;
  updateVisibility = false;
  hideShared = false;
  description = ''
  descriptionImage = ''
  constructor(
    public store: Store<MicroservicesState>,
    public secondStore: Store<SubscriptionState>,
    public microserviceService: MicroserviceService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<MicroserviceModalComponent>,
    public dialog: MatDialog,
    private cdRef: ChangeDetectorRef,
    private organisationService: OrganizationService,
    private teamsService: TeamService,
    private subscriptionService: SubscriptionService
  ) { }

  ngOnInit(): void {
    if (this.data.action.update) {

      this.update = true
      if (this.data.microservice.owner !== localStorage.getItem('userEmail')) {
        this.updateVisibility = true;
      }
      if (this.data.microservice.type !== 'shared') {
        this.hideShared = true;
      }
    }
    if (this.data.microservice.constructor === Object) {
      this.microservice = { ...this.data.microservice };
      const htmlDescription = this.microservice.description;
      const div = document.createElement('div');
      div.innerHTML = htmlDescription;
      this.description = div.textContent || div.innerText || '';
      this.descriptionImage = div.textContent || div.innerText || '';

    }

    this.swaggers =
      this.data.swaggers !== undefined ? [...this.data.swaggers] : [];
    this.swaggers.forEach((element) => {
      element.content = null;
    });
    setTimeout(() => {
      this.getTeams();

    }, 1);

    this.filteredOptions = this.myControl.valueChanges
      .pipe(
        startWith(''),
        map(value => this._filter(value))
      );

  }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }
  private _filter(value: string): any[] {
    const filterValue = value.toLowerCase();
    return this.teamNames.filter((option: any) => option.name.toLowerCase().includes(filterValue));
  }

  getTeams() {
    this.organisationService.getOrganisation().subscribe(organ => {
      if (organ.length !== 0) {
        this.organisationService.getMyOrganizationTeams().subscribe((teams: any[]) => {
          if (teams) {
            this.teamNames = teams;
          }
        })
      }
    })
  }

  add(event: MatChipInputEvent): void {
    const input = event.input;
    const value = event.value;

    // Add our fruit
    if ((value || '').trim()) {
      this.teams.push({ name: value.trim() });
    }

    // Reset the input value
    if (input) {
      input.value = '';
    }
  }

  remove(name: any): void {
    const index = this.teams.indexOf(name);
    const ind = this.teamNames.findIndex(t => t.name === name.name);
    const id = this.teamNames[ind].id;
    const secondInd = this.microservice.teamIds.findIndex(t => t === id);
    if (secondInd >= 0) {
      this.microservice.teamIds.splice(secondInd, 1);
    }

    if (index >= 0) {
      this.teams.splice(index, 1);
    }
  }
  Save() {
    if (this.microservice.type === 'shared' && this.microservice.teamIds.length === 0) {
      this.sharedError = true;
    } else {
      if (this.data.action.update) {
        if (this.microservice.type !== 'shared') {
          this.microservice.teamIds = [];
          this.data.microservice.teamIds.forEach(element => {
            this.teamsService.getMembersByTeam(element).subscribe((members: any[]) => {
              members.forEach(el => {
                if (el.email !== localStorage.getItem('userEmail')) {
                  this.subscriptionService.deleteMembersSubscription(el.email, this.data.microservice.id).subscribe(d => {

                  })
                }
              });
            })
          });

        }
      }
      const newForm = new FormData();
      if (this.description !== this.descriptionImage) {
        this.microservice.description = this.description
      }
      const obj = { microservice: this.microservice, swaggers: [], modification: '' };
      this.swaggers.forEach((swagger, index) => {
        if (swagger.url == null && swagger.file !== null && swagger.file !== {}) {
          newForm.append('files', swagger.file);
          newForm.append('environments', swagger.environment);
        } else if (swagger.url) obj.swaggers.push(swagger);
      });

      if (this.data.action.create) {
        obj.microservice.owner = localStorage.getItem('userEmail')
        newForm.append(
          'data',
          new Blob([JSON.stringify(obj)], { type: 'application/json' })
        );

        this.microserviceService
          .checkMicroserviceNameUnicity(this.microservice.title)
          .subscribe((res) => {
            if (res === false) {
              if (this.swaggers.length === 0) {
                this.containsSwagger = true;
                this.existingName = false;
              } else {
                this.store.dispatch(
                  new microservice.AddMicroserviceAttachFileRequest(newForm)
                );
              }
            } else {
              this.existingName = true;
              this.containsSwagger = false;
            }
          });

      }
      if (this.data.action.update) {
        if (this.data.microservice.title !== this.microservice.title)
          obj.modification += ' title ';
        if (this.data.microservice.description !== this.microservice.description)
          obj.modification += ' description ';
        if (this.data.microservice.type !== this.microservice.type)
          obj.modification += ' type ';
        if (this.data.swaggers.length !== this.swaggers.length) {
          obj.modification += ' swaggers ';
        }
        newForm.append(
          'data',
          new Blob([JSON.stringify(obj)], { type: 'application/json' })
        );
        console.log(obj.swaggers)
        this.store.dispatch(
          new microservice.UpdateMicroserviceAttachFileRequest(newForm)
        );

      }
    }

  }
  addOption(option, id) {
    if ((option || '').trim() && (!this.teams.some(team => team.name === option))) {
      this.teams.push({ name: option.trim() });
      this.microservice.teamIds.push(id);
      console.log(this.teamInput.nativeElement.value);
      this.teamInput.nativeElement.value = '';
    }
    else {
      this.teamInput.nativeElement.value = '';
    }
  }
  importSwaggerOnExistingContainer() {
    const len = this.uploader.queue.length - 1;
    this.fileName = this.uploader.queue[len]._file.name;
    this.swagger.file = this.uploader.queue[len]._file;
  }

  /* ADD Swagger For Different Environments Before Submit */
  addNewSwagger() {
    const formData = new FormData();
    formData.append('file', this.swagger.file);
    formData.append(
      'swagger',
      new Blob([JSON.stringify(this.swagger)], {
        type: 'application/json',
      })
    );

    if ((this.swagger.url || this.swagger.file) && this.swagger.environment) {
      this.microserviceService.checkSwaggerURL(formData).subscribe(
        (result) => {
          if (this.swagger.file)
            result = { ...result, file: this.swagger.file };
          console.log('result ', result);
          this.swaggers.push(result);
          this.swagger = new SwaggerDto();
          this.swagger.file = null;
          this.invalidSwaggerUrl = false;
        },
        (err) => {
          this.invalidSwaggerUrl = true;
        }
      );
    }
  }

  /* DELETE Swagger On Click Before Submit */
  deleteSwaggerRow(i: number) {
    this.swaggers.splice(i, 1);
  }
}
