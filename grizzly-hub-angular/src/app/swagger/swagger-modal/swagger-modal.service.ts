import { MatDialogRef, MatDialog } from '@angular/material/dialog';
import { SwaggerModalComponent } from './swagger-modal.component';
// import { SwaggerDiffComponent } from '../swagger-diff/swagger-diff.component';

export class SwaggerModalService {

    private dialogRef: MatDialogRef<SwaggerModalComponent>;
 // private dialogRefDiff: MatDialogRef<SwaggerDiffComponent>;
    constructor(public dialog: MatDialog) { }

    openInspector(url) {
        this.dialogRef = this.dialog.open(SwaggerModalComponent,
            {
                width: '75%',
                height: '85vh',
                position: {
                },
                hasBackdrop: true,
                data: {
                    url
                }
            });
        return this.dialogRef;
    }

    // openDiff(id) {
    //     this.dialogRefDiff = this.dialog.open(SwaggerDiffComponent,
    //         {
    //             width: '75%',
    //             height: '85vh',
    //             position: {
    //             },
    //             hasBackdrop: true,
    //             data: {
    //                 idOldSwagger:id
    //             }
    //         });
    //     return this.dialogRef;
    // }
}
