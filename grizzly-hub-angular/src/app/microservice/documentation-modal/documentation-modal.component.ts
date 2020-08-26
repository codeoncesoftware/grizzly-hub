import { Component, OnInit, Inject, ViewChild, ElementRef, ChangeDetectorRef, AfterViewChecked, ViewEncapsulation } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import * as ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import { ChangeEvent } from '@ckeditor/ckeditor5-angular';
import { Store } from '@ngrx/store';
import { MicroservicesState } from 'src/app/store/microservice/mircoservice.state';
import * as microservice from '../../store/microservice/microservice.actions';



@Component({
  selector: 'app-documentation-modal',
  templateUrl: './documentation-modal.component.html',
  styleUrls: ['./documentation-modal.component.scss']
})
export class DocumentationModalComponent implements OnInit, AfterViewChecked {

  Editor = ClassicEditor;
  endpoints = [];
  endpointsToDocument = [];
  show = false;
  endpointName: string;
  operation: string;
  curl: string;
  baseUrl: string;
  endpointsToUpdate = [];
  postEndpoints = [];
  getEndpoints = [];
  putEndpoints = [];
  deleteEndpoints = [];
  checkedGetEndpoints = [];
  checkedPostEndpoints = [];
  checkedPutEndpoints = [];
  checkedDeleteEndpoints = [];

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
    private store: Store<MicroservicesState>,
    public dialogRef: MatDialogRef<DocumentationModalComponent>,
    private cdRef: ChangeDetectorRef,) { }
  ngOnInit(): void {
    const content = JSON.parse(this.data.swagger.content);

    const array = Object.keys(content.paths);
    this.baseUrl = 'https://' + content.host + content.basePath

    for (let index = 0; index < array.length; index++) {
      const endpoint = array[index];
      const operation = Object.keys((Object.values(JSON.parse(this.data.swagger.content).paths)[index]))[0];
      this.endpoints.push({ endpoint, operation });
    }
    console.log(this.data.microservice)
    this.endpoints.forEach(e => {
      if (this.data.microservice.documentation.some(doc => doc.endpoint === e.endpoint && doc.operation === e.operation)) {
        const index = this.data.microservice.documentation.findIndex(doc => doc.endpoint === e.endpoint && doc.operation === e.operation);
        const obj = this.data.microservice.documentation[index];
        this.endpointsToDocument.push(obj);
        this.endpointsToUpdate.push(true);
      } else {
        this.endpointsToUpdate.push(false);
      }
    });
    this.endpoints.forEach(el => {
      if (el.operation === 'post') {
        this.postEndpoints.push(el);
        if (this.endpointsToDocument.some(elem => elem.endpoint === el.endpoint)) {
          this.checkedPostEndpoints.push(true);
        } else {
          this.checkedPostEndpoints.push(false);
        }
      }
      if (el.operation === 'delete') {
        this.deleteEndpoints.push(el);
        if (this.endpointsToDocument.some(elem => elem.endpoint === el.endpoint)) {
          this.checkedDeleteEndpoints.push(true);
        } else {
          this.checkedDeleteEndpoints.push(false);
        }
      }
      if (el.operation === 'put') {
        this.putEndpoints.push(el);
        if (this.endpointsToDocument.some(elem => elem.endpoint === el.endpoint)) {
          this.checkedPutEndpoints.push(true);
        } else {
          this.checkedPutEndpoints.push(false);
        }
      }
      if (el.operation === 'get') {
        this.getEndpoints.push(el);
        if (this.endpointsToDocument.some(elem => elem.endpoint === el.endpoint)) {
          this.checkedGetEndpoints.push(true);
        } else {
          this.checkedGetEndpoints.push(false);
        }
      }
    });
    console.log(this.endpointsToDocument)
  }

  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }

  showEdior(name: string, operation: string) {
    this.endpointName = name;
    this.operation = operation;
    console.log(this.endpointsToDocument);
    if (this.endpointsToDocument.some(m => m.endpoint === name && m.operation === operation)) {
      const index = this.endpointsToDocument.findIndex(s => s.endpoint === name && s.operation === operation);
      this.curl = this.endpointsToDocument[index].doc;
    } else {
      this.curl = '<p> curl --header  "Content-Type: application/json"  --request  ' + operation.toUpperCase() + '  ' + this.baseUrl + name + '</p>'
    }

    this.show = !this.show
    window.scroll(100, 100);
  }
  hideEditor() {
    this.show = false;
  }
  setColor(operation) {
    let color;
    if (operation === 'post') {
      color = 'primary'
    }
    if (operation === 'get') {
      color = 'warn'
    }
    return color;
  }
  addEndpoint(endpoint?) {
    const index = this.endpointsToDocument.findIndex(e => e.endpoint === endpoint.endpoint && e.operation === endpoint.operation);
    console.log(index);
    if (index < 0) {
      const curl = '<p> curl --header  "Content-Type: application/json"  --request  ' + endpoint.operation.toUpperCase() + '  ' + this.baseUrl + endpoint.endpoint + '</p>';
      const completeDoc = {
        ...endpoint,
        doc: curl
      }
      this.endpointsToDocument.push(completeDoc);

    } else {
      this.endpointsToDocument.splice(index, 1);

    }

  }
  documentMicroservice(endpointName, operation) {
    this.changeDoc(endpointName, operation)
    this.data.microservice.documentation = this.endpointsToDocument;
    const newForm = new FormData();
    const obj = { microservice: this.data.microservice, swaggers: this.data.swaggers, modification: ' doc ' };
    newForm.append(
      'data',
      new Blob([JSON.stringify(obj)], { type: 'application/json' })
    );
    this.store.dispatch(new microservice.UpdateMicroserviceAttachFileRequest(newForm));
  }
  changeDoc(endpointName, operation) {
    const index = this.endpointsToDocument.findIndex(s => s.endpoint === endpointName && s.operation === operation);
    if (index >= 0) {
      this.endpointsToDocument[index].doc = this.curl;
    }
    this.show = false;
  }
  onChange({ editor }: ChangeEvent) {
    const data = editor.getData();
    this.curl = data;
  }

}
