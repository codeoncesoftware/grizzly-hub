import { Injectable } from '@angular/core';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { OrganizationModalComponent } from './organization-modal.component';


@Injectable({
    providedIn: 'root'
})
export class OrganizationModalService {

    public dialogRef: MatDialogRef<OrganizationModalComponent>;

    constructor(public dialog: MatDialog) { }
    public close() {
        this.dialog.closeAll();
    }
}