import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiffTreeModalComponent } from './diff-tree-modal.component';

@Injectable({
  providedIn: 'root'
})
export class DiffTreeService {

  constructor(
    public diffTreeDialog: MatDialog

  ) { }
  openDiffTree(dataTree) {
    return this.diffTreeDialog.open(DiffTreeModalComponent,
      {
        width: '75%',
        height: '85vh',
        position: {
        },
        hasBackdrop: true,
        data: {
          dataTree
        }
      }
    )
  }
}
