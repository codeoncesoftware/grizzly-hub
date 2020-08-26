import { Injectable } from '@angular/core';

import { ConfirmModalComponent } from './confirm-modal.component';
import { MatDialogRef, MatDialog } from '@angular/material/dialog';

@Injectable({
    providedIn: 'root'
})

export class ConfirmModalService {

    private dialogRef: MatDialogRef<ConfirmModalComponent>;

    constructor(public dialog: MatDialog) { }

    openConfirm(titleKey: string, msgKey: string = '', params: {}) {
        this.dialogRef = this.dialog.open(ConfirmModalComponent,
            {
                width: '50%',
                position: {
                    top: '200px',
                },
                data: {
                    title: titleKey,
                    message: msgKey,
                    params
                }
            });
        return this.dialogRef;
    }
}
