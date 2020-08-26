import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import * as _ from 'lodash';

@Component({
  selector: 'app-diff-tree-modal',
  templateUrl: './diff-tree-modal.component.html',
  styleUrls: ['./diff-tree-modal.component.sass']
})
export class DiffTreeModalComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<DiffTreeModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) { }

  public tree: {
    nestedDataSource: {},
    treeControl: {},
    hasChild: boolean
  };
  ngOnInit(): void {
    console.log('tree', this.data);
    this.tree = { ...this.data.dataTree };
  }
  toUpperCase(word) {
    return _.startCase(word);
  }
  close() {
    this.dialogRef.close();
  }
}
