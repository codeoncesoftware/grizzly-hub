import { Injectable } from '@angular/core';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { MicroserviceModalComponent } from './microservice-modal.component';


@Injectable({
    providedIn: 'root'
})
export class MicroserviceModalService {

    public dialogRef: MatDialogRef<MicroserviceModalComponent>;

    constructor(public dialog: MatDialog) { }
    public close() {
        this.dialog.closeAll();
    }
}