import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, Input, OnInit } from '@angular/core';
import { BetSubjectService } from '../../service/bet-subject.service';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { BetSubjectType } from 'src/app/model/enum/bet-subject-type';
import { BetSubject, EmptyBetSubject } from 'src/app/model/bet-subject';
import { BetSubjectDialogData } from 'src/app/model/bet-subject-dialog-data';

@Component({
  selector: 'app-bet-subject',
  templateUrl: './bet-subject.component.html',
  styleUrls: ['./bet-subject.component.scss']
})
export class BetSubjectComponent implements OnInit {
  betSubjects: BetSubject[] = [];

  constructor(
    private betSubjectService: BetSubjectService, 
    public dialogRef: MatDialogRef<BetSubjectComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: BetSubjectDialogData){}

  ngOnInit(): void {
    this.getBetSubjects();
  }

  getBetSubjects(): void {
    this.betSubjectService.getBetSubjects(this.dialogData.type, this.dialogData.raceId).subscribe(
      (betSubjects) => {
        this.betSubjects = betSubjects;
        this.filterSubjects();
      }
    );
  }

  filterSubjects(): void {
    this.betSubjects = this.betSubjects.filter(betSubject => {
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
