import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { SwaggerDiffService } from './swagger-diff.service';
import { DiffContentModalComponent } from './swagger-diff-content/diff-content-modal/diff-content-modal.component';
import { ToastrService } from 'ngx-toastr';
import { AppTranslateService } from 'src/app/shared/services/app-translate-service';
import { DiffContentModalService } from './swagger-diff-content/diff-content-modal.service';

@Component({
  selector: 'app-swagger-diff',
  templateUrl: './swagger-diff.component.html',
  styleUrls: ['./swagger-diff.component.sass']
})
export class SwaggerDiffComponent implements OnInit {

  constructor(
    public diffContentDialog: MatDialog,
    public dialogRef: MatDialogRef<SwaggerDiffComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public swaggerDiffService: SwaggerDiffService,
    public toaster: ToastrService,
    private translateService: AppTranslateService,
    private diffService: DiffContentModalService

  ) { }


  public id: string;
  public swaggers: any[];
  public selectedOptions: any[];
  public diffContent = {};
  public selectedSwaggers: any;
  public invalidSwaggerTypes = false;

  ngOnInit(): void {
    console.log(this.data);
    this.id = this.data.selected;
    this.swaggers = [...this.data.swaggers];
    this.selectedOptions = this.mapSwagger();
  }

  mapSwagger() {
    return this.swaggers.map(swagger => {
      return { swagger, selected: swagger.id === this.id }
    })
  }

  onListChange(list) {
    this.selectedSwaggers = list.selectedOptions.selected.map(item => item.value);
  }
  showDiffs() {
    const oldId = this.selectedSwaggers[0];
    const newId = this.selectedSwaggers[1];
    this.diffService.openDiffModal(oldId, newId,'compare');
    this.dialogRef.close();
  }


}
