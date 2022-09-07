import { HttpErrorResponse } from "@angular/common/http";
import { ErrorHandler, Injectable, NgZone } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import {MatSnackBar} from '@angular/material/snack-bar';
import { Router } from "@angular/router";
import { AccessDeniedDialogComponent } from "./component/dialog/access-denied-dialog/access-denied-dialog.component";

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
    constructor (private _snackBar: MatSnackBar, private zone: NgZone, private dialog: MatDialog, private router: Router) {}

    handleError(error: any): void {
        if(error instanceof HttpErrorResponse) {
            switch (error.error.message) {
                case 'Token is invalid':
                    this.router.navigate(['/login'])
                    //this.router.navigateByUrl("/login")
                    break;
                case 'Access to this object is forbidden.':
                    this.zone.run(() => {
                        const dialogRef = this.dialog.open(AccessDeniedDialogComponent);
                        dialogRef?.afterClosed().subscribe(() => {this.router.navigateByUrl("/");});
                    });
                    break;
                default:
                    this.zone.run(() => {
                        this._snackBar.open('Unable to connect to server', '', {duration: 2500, verticalPosition: 'bottom'});
                    });
                    break;
            }
        }        
        console.error(error);
    }
}