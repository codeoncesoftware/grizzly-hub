import { Component, OnInit, Inject, ChangeDetectorRef } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { MicroservicesState } from 'src/app/store/microservice/mircoservice.state';
import * as microservice from '../../store/microservice/microservice.actions';
import { MicroserviceService } from '../microservice.service';
@Component({
  selector: 'app-faq-modal',
  templateUrl: './faq-modal.component.html',
  styleUrls: ['./faq-modal.component.sass']
})
export class FaqModalComponent implements OnInit {

  faqObj = {
    response: '',
    questionTitle: ''

  };
  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
    public store: Store<MicroservicesState>,
    public dialogRef: MatDialogRef<FaqModalComponent>,
    private cdRef: ChangeDetectorRef,
  ) { }

  ngOnInit(): void {
    if (this.data.action.update) {
      this.faqObj = Object.assign({}, this.data.faq);
    }
  }
  Save() {
    if (this.data.action.create) {
      this.data.microservice.faq.push(this.faqObj);
      const newForm = new FormData();
      const obj = { microservice: this.data.microservice, swaggers: this.data.swaggers , modification : ' faq '};
      newForm.append(
        'data',
        new Blob([JSON.stringify(obj)], { type: 'application/json' })
      );
      this.store.dispatch(new microservice.UpdateMicroserviceAttachFileRequest(newForm ));
    }
    if (this.data.action.update) {
      const index = this.data.microservice.faq.findIndex(f => f.questionTitle === this.data.faq.questionTitle && f.response === this.data.faq.response)
      this.data.microservice.faq[index] = this.faqObj
      const newForm = new FormData();
      const obj = { microservice: this.data.microservice, swaggers: this.data.swaggers };
      newForm.append(
        'data',
        new Blob([JSON.stringify(obj)], { type: 'application/json' })
      );
      this.store.dispatch(new microservice.UpdateMicroserviceAttachFileRequest(newForm));

    }

  }

}
