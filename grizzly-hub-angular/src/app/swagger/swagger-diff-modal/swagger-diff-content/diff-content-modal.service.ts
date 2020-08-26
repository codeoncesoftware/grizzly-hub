import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiffContentModalComponent } from './diff-content-modal/diff-content-modal.component';
import { SwaggerDiffService } from '../swagger-diff.service';
import { AppTranslateService } from 'src/app/shared/services/app-translate-service';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class DiffContentModalService {

  constructor(
    public diffContentDialog: MatDialog,
    public diffService: SwaggerDiffService,
    private translateService: AppTranslateService,
    public toaster: ToastrService,

  ) { }

  openDiffModal(oldId, newId, mode) {
    const obj = { oldSwaggerId: oldId, newSwaggerId: newId };
    console.log(oldId);
    console.log(newId);
    if (mode === 'notification') {
      this.diffService.getSwaggerDiifs(obj).subscribe(data => {
        if (data) {
          return this.showDiffContentModal(data);
        }
      }, (err) => {
        return this.toaster.error(this.translateService.getMessage('diff.invalidTypes'));
      })
    } else {
      this.diffService.getSwaggerCompare(obj).subscribe(data => {
        if (data) {
          return this.showDiffContentModal(data);
        }
      }, (err) => {
        return this.toaster.error(this.translateService.getMessage('diff.invalidTypes'));
      })

    }

  }

  showDiffContentModal(content) {
    return this.diffContentDialog.open(DiffContentModalComponent,
      {
        width: '75%',
        height: '85vh',
        position: {
        },
        hasBackdrop: true,
        data: {
          diffContent: content
        }
      }
    )
  }
}
