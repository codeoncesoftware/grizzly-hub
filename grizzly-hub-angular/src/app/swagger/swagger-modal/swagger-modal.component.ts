import { Component, OnInit, Inject, ElementRef } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import SwaggerUI from 'swagger-ui';

@Component({
  selector: 'app-swagger-modal',
  templateUrl: './swagger-modal.component.html',
  styleUrls: ['./swagger-modal.component.sass']
})
export class SwaggerModalComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private el: ElementRef,
  ) { }
  url: any
  swaggerBaseUrl = '';
  ui: any;
  ngOnInit(): void {
    console.log(this.data.url);
    this.openInspector();
  }

  openInspector() {
    this.ui = SwaggerUI({
      url: this.data.url,
      domNode: this.el.nativeElement.querySelector('.swagger-container'),
      deepLinking: true,
      presets: [
        SwaggerUI.presets.apis
      ],
    });
    window.scrollTo({ top: 0 });
  }
}
