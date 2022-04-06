import { Component, Inject, Input, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { User } from 'src/app/model/user';
import { UserData } from 'src/app/model/user-data';
import { AppService } from 'src/app/service/app.service';

@Component({
  selector: 'app-user-dialog',
  templateUrl: './user-dialog.component.html',
  styleUrls: ['./user-dialog.component.scss']
})
export class UserDialogComponent {

  

  constructor(
    private router: Router,
    public appService: AppService,
    private dialogRef: MatDialogRef<UserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public userData: UserData) {
  }

  setLanguage(language: string) {
    this.appService.setLanguage(language);
    this.userData.selectedLanguage = language;
  }

  logout() {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('selectedLeague');
    localStorage.removeItem('userData');
    this.dialogRef.close()
    this.router.navigateByUrl('/login')
  }

}
