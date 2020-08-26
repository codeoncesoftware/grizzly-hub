import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { SlideInOutAnimation } from '../shared/animations';
import { MicroservicesState } from '../store/microservice/mircoservice.state';
import { MatDialog } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { Microservice } from '../shared/models/Microservice';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from '../shared/message-modal/message.service';

import * as microservice from '../store/microservice/microservice.actions';
import { MicroserviceModalComponent } from './microservice-modal/microservice-modal.component';
import { MicroserviceService } from './microservice.service';

import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { DocumentationModalComponent } from './documentation-modal/documentation-modal.component';
import { FaqModalComponent } from './faq-modal/faq-modal.component';

@Component({
  selector: 'app-microservice',
  templateUrl: './microservice.component.html',
  styleUrls: ['./microservice.component.scss'],
  animations: [SlideInOutAnimation],
})
export class MicroserviceComponent implements OnInit {
  microservice: Microservice;
  swaggers: any[] = [];
  microservices: any[];
  toggleMicroserviceDetails = false;
  swaggerName: string;
  swaggerType: string;
  swaggerAuth: string[] = [];
  swaggerLastVersion: string;
  swaggerInProd: string;
  swagger: any;
  swaggerDetails: any = {};
  showAuthenticationTypes = false;
  public Editor = ClassicEditor;
  showDescription = false;
  editAndDelete = false;
  constructor(
    private store: Store<MicroservicesState>,
    private dialog: MatDialog,
    private microserviceService: MicroserviceService,
    private messageBoxService: MessageService,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit(): void {
    if (localStorage.getItem('token')) {
      const personnal = 'personnal';
      const shared = 'shared';
      this.store.select('microservice').subscribe((resState) => {
        // tslint:disable-next-line: no-string-literal
        this.microservices = resState[shared].concat(resState[personnal], resState['publique']);
        this.activatedRoute.params.subscribe((routeParams) => {
          for (const element of this.microservices) {
            if (element.id === routeParams.id) {
              console.log('microservice loaded', element);
              this.microservice = element;
              if (this.microservice.faq === null) {
                this.microservice.faq = []
              }
              if (this.microservice.documentation === null) {
                this.microservice.documentation = []
              }
              break;
            }
          } if (this.microservice?.owner === localStorage.getItem('userEmail') || this.microservice?.type === 'shared') {
            this.editAndDelete = true;
          }
          this.microserviceService.getSwaggerDetails(routeParams.id).subscribe((data: any) => {
            console.log(data)
            this.swaggerAuth = []
            this.swaggerDetails = data;
            if (data.swaggerAuth)
              this.swaggerAuth = Object.values(data.swaggerAuth)
            console.log(this.swaggerAuth)
          })
          this.microserviceService
            .getSwaggersByMicroserviceId(routeParams.id)
            .subscribe((data: any[]) => {
              this.swaggers = [];
              if (data.length > 0) {
                this.swaggers = data;
                this.swagger = data[0]
              }
              if (data.length === 0) {
                this.swaggers = [];
              }
            });
        });
      });
    } else {
      this.activatedRoute.params.subscribe((routeParams) => {
          this.loaddMicroserviceByName(routeParams.id.split('_').join(' '))

        // if(this.microservice.owner === localStorage.getItem('userEmail') || this.microservice.type === 'shared'){
        //   this.editAndDelete = true;
        // }

      });
    }
  }

  loaddMicroserviceByName(name) {
    console.log(name)
    this.microserviceService.getMiroserviceByName(name).subscribe(res => {
      this.microservice = res
      console.log(this.microservice);

      this.microserviceService.getSwaggerDetailsPublic(res.id).subscribe((data: any) => {
        console.log(data)
        this.swaggerAuth = []
        this.swaggerDetails = data;
        if (data.swaggerAuth)
          this.swaggerAuth = Object.values(data.swaggerAuth)
        console.log(this.swaggerAuth)
      })
      this.microserviceService
        .getSwaggersByMicroserviceIdPublic(res.id)
        .subscribe((data: any[]) => {
          this.swaggers = [];
          if (data.length > 0) {
            this.swaggers = data;
            this.swagger = data[0]
          }
          if (data.length === 0) {
            this.swaggers = [];
          }
        });
    })
  }

  loadMicroservice(id) {
    for (const element of this.microservices) {
      if (element.id === id) {
        console.log('microservice loaded', element);
        this.microservice = element;
        if (this.microservice.faq === null) {
          this.microservice.faq = []
        }
        if (this.microservice.documentation === null) {
          this.microservice.documentation = []
        }
        break;
      }
    }
  }

  showMicroserviceDetails() {
    this.toggleMicroserviceDetails = !this.toggleMicroserviceDetails;
  }

  // Open the modal to get confirmation for delete
  public openConfirmDeleteDialog() {
    this.messageBoxService
      .openWarning(
        'popups.microservice.delete.title',
        'popups.microservice.delete.msg',
        {
          title: this.microservice.title,
          info: {
            msg: 'messageBox.microservice.delete',
            infos: [
              'messageBox.microservice.msgDeleteAllVersion',
              'messageBox.microservice.msgDeleteAllFiles',
            ],
          },
        }
      )
      .afterClosed()
      .subscribe((data) => {
        if (data) {
          this.store.dispatch(
            new microservice.DeleteMicroservice(this.microservice.id)
          );
        }
      });
  }

  openMicroserviceModal() {
    this.dialog.open(MicroserviceModalComponent, {
      // Modal configuration
      width: '80%',
      height: '90vh',
      position: {
        top: '9vh',
      },
      data: {
        microservice: this.microservice,
        swaggers: this.swaggers,
        action: {
          update: true,
          create: false,
          msg: 'popups.microservice.edit',
        },
      },
    });
  }
  openDocumentationModal() {
    this.dialog.open(DocumentationModalComponent, {
      // Modal configuration
      width: '1220px',
      height: '90vh',
      position: {
        top: '9vh',
      },
      data: {
        action: {
          update: false,
          create: true,
          msg: 'popups.faq.add',
        },
        microservice: this.microservice,
        swaggers: this.swaggers,
        swagger: this.swagger,
      },
    });
  }

  openFAQModal() {
    this.dialog.open(FaqModalComponent, {
      // Modal configuration
      width: '60%',
      height: '70%',
      position: {
        top: '12vh',
      },
      data: {
        action: {
          update: false,
          create: true,
          msg: 'popups.faq.add',
        },
        microservice: this.microservice,
        swaggers: this.swaggers,
      },
    });
  }
  deleteQuestion(questionTitle, response) {
    this.messageBoxService
      .openWarning(
        'popups.faq.delete.title',
        'popups.faq.delete.msg',
        {
          title: this.microservice.title,
          info: {
            msg: 'messageBox.faq.delete'
          },
        }
      )
      .afterClosed()
      .subscribe((data) => {
        if (data) {
          const indexQuestion = this.microservice.faq.findIndex(q => q.questionTitle === questionTitle && q.response === response)
          this.microservice.faq.splice(indexQuestion, 1);
          const newForm = new FormData();
          const obj = { microservice: this.microservice, swaggers: this.swaggers, modification: ' faq ' };
          newForm.append(
            'data',
            new Blob([JSON.stringify(obj)], { type: 'application/json' })
          );
          this.store.dispatch(
            new microservice.UpdateMicroserviceAttachFileRequest(newForm)
          );
        }
      });
  }
  openFAQModalToEdit(questionTitle, response) {
    this.dialog.open(FaqModalComponent, {
      // Modal configuration
      width: '60%',
      height: '70%',
      position: {
        top: '12vh',
      },
      data: {
        action: {
          update: true,
          create: false,
          msg: 'popups.faq.edit',
        },
        microservice: this.microservice,
        swaggers: this.swaggers,
        faq: { questionTitle, response }
      },
    });
  }
  editDescription() {
    this.showDescription = !this.showDescription;
  }
  updateDescription() {
    const newForm = new FormData();
    const obj = { microservice: this.microservice, swaggers: this.swaggers, modification: ' description ' };
    console.log(this.microservice)
    newForm.append(
      'data',
      new Blob([JSON.stringify(obj)], { type: 'application/json' })
    );
    this.store.dispatch(
      new microservice.UpdateMicroserviceAttachFileRequest(newForm)
    );
    this.showDescription = false
  }
}