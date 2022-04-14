import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-forgot-password-dialog',
  templateUrl: './forgot-password-dialog.component.html',
  styleUrls: ['./forgot-password-dialog.component.scss']
})
export class ForgotPasswordDialogComponent {
  public password: string = "";

  constructor(
    private userService: UserService,
    public dialogRef: MatDialogRef<ForgotPasswordDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public username: string = "") { }

  onSubmit() {
    this.userService.requestPasswordChange(this.username, this.password).subscribe({ 
      next: success => {
        if (success) {
          this.dialogRef.close()
        }
      },
      error: (error: HttpErrorResponse) => {
        alert('User does not exist.');
      }
    });
  }

}
