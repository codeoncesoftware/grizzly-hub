import { Component, ElementRef, OnInit } from '@angular/core';
import SwaggerUI from 'swagger-ui';
import { SlideInOutAnimation } from '../../shared/animations';
import { ActivatedRoute } from '@angular/router';
import { MicroserviceService } from 'src/app/microservice/microservice.service';
import { Store } from '@ngrx/store';
import { MicroservicesState } from 'src/app/store/microservice/mircoservice.state';
import { MatDialog } from '@angular/material/dialog';
import { SwaggerDiffComponent } from '../swagger-diff-modal/swagger-diff.component';
import { environment } from 'src/environments/environment';
import { DashboardService } from 'src/app/layout/dashboard/dashboard.service';
@Component({
  selector: 'app-swagger-container',
  templateUrl: './swagger-container.component.html',
  styleUrls: ['./swagger-container.component.scss'],
  animations: [SlideInOutAnimation],
})
export class SwaggerContainerComponent implements OnInit {
  containers: any[] = [];
  swaggerVersions = [];
  selectedIndex = 0;
  toggleContainerDetails = true;
  showInspector = true;
  ui: any;
  baseUrl: string = environment.baseUrl + '/api/microservice';
  containersResult: any[] = []

  constructor(
    private store: Store<MicroservicesState>,
    private el: ElementRef,
    private activatedRoute: ActivatedRoute,
    private diffDialog: MatDialog,
    private microserviceService: MicroserviceService,
    private dashboardService: DashboardService
  ) { }
  ngOnInit(): void {
    if (localStorage.getItem('token')) {
      const shared = 'shared';
      const personnal = 'personnal';
      const publicAcessor = 'publique';
      this.activatedRoute.paramMap.subscribe((params: any) => {
        this.store.select('microservice').subscribe((resState) => {
          const microservices = [...resState[shared]].concat([...resState[personnal]], [...resState[publicAcessor]]);
          const microserviceIndex = microservices.findIndex(
            (m) => m.id === params.params.id
          );
          const microservice = microservices[microserviceIndex];
          this.loadSwaggerUI(microservice)
        });
      });
    } else {
      this.activatedRoute.paramMap.subscribe((params: any) => {
        console.log(params.params.id);

        this.microserviceService.getMiroserviceByName(params.params.id.split('_').join(' ')).subscribe(res => {
          this.loadSwaggerUI(res)
        })
      })
    }
  }

  loadSwaggerUI(microservice) {
    if (microservice) {
      this.containers = microservice.swaggersVersions.sort((a, b) => (a.environment > b.environment) ? 1 : (a.environment === b.environment) ? ((a.lastUpdateDate < b.lastUpdateDate) ? 1 : -1) : -1);
      this.containersResult = [];
      this.containersResult.push(this.containers[0]);
      for (let index = 1; index < this.containers.length; index++) {
        const element = this.containers[index];
        if (!this.containersResult.some(e => e.environment === element.environment)) {
          this.containersResult.push(element)
        }
      }
      setTimeout(() => {
        this.openInspector(window.location.origin + '/api/microservice/content/' + this.containersResult[0]?.id);
      }, 1);
    }
  }

  openDiffModal() {
    if (localStorage.getItem('token')) {
      this.diffDialog.open(SwaggerDiffComponent, {
        width: '75%',
        height: '85vh',
        position: {},
        hasBackdrop: true,
        data: {
          swaggers: this.containersResult,
          selected: null,
        },
      });
    } else this.dashboardService.tryToConnect()
  }
  hideIfEmpty() {
    if (this.containers.length !== 0) {
      return true;
    }
    return false;
  }
  convertSwagger(swaggers) {
    if (swaggers) {
      swaggers.map((swagger) => {
        swagger.content = JSON.parse(swagger.content);
      });
    }

    return swaggers;
  }
  loadSwaggers(event) {
    console.log(this.containers[event.index]);
    this.openInspector(window.location.origin + '/api/microservice/content/' + this.containersResult[event.index].id);
  }
  showContainerDetails() {
    this.toggleContainerDetails = !this.toggleContainerDetails;
  }
  openInspector(theUrl) {
    console.log('ioj')
    if (theUrl) {
      this.showInspector = true;
      this.ui = SwaggerUI({
        url: theUrl,
        dom_id: '#swagger-ui',
        permalinks: false,
        // domNode: this.el.nativeElement.querySelector('.swagger-container'),
        deepLinking: false,
        presets: [SwaggerUI.presets.apis],
      });
    }
  }
}