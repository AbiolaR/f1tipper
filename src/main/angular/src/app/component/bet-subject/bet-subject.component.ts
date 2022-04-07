import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BetSubject, EmptyBetSubject } from 'src/app/model/bet-subject';
import { BetSubjectDialogData } from 'src/app/model/bet-subject-dialog-data';

@Component({
  selector: 'app-bet-subject',
  templateUrl: './bet-subject.component.html',
  styleUrls: ['./bet-subject.component.scss']
})
export class BetSubjectComponent {
  betSubjects: BetSubject[] = [];

  constructor(
    public dialogRef: MatDialogRef<BetSubjectComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: BetSubjectDialogData){
      this.assignAndFilterBetSubjects();
    }    


  assignAndFilterBetSubjects(): void {
    this.betSubjects = this.dialogData.betSubjects.filter(betSubject => {
      return !this.dialogData.excludeBetSubjects.includes(betSubject.id);
    })
  }

  returnBetSubject(betSubject: BetSubject) {
    this.dialogRef.close(betSubject)
  }

  removeBetSubject() {
    this.returnBetSubject(new EmptyBetSubject())
  }

}