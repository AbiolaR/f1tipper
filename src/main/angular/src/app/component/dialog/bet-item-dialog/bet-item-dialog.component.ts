import { Component, OnInit, Inject } from '@angular/core';
import { BetService } from '../../../service/bet.service';
import {MAT_DIALOG_DATA, MatDialog} from '@angular/material/dialog';
import { BetItem as BetItem } from 'src/app/model/bet-item';
import { BetItemData } from 'src/app/model/bet-item-data';
import { BetSubjectComponent } from '../../bet-subject/bet-subject.component';
import { BetItemStatus } from 'src/app/model/enum/bet-item-status';
import { BetSubjectType } from 'src/app/model/enum/bet-subject-type';
import { BetSubject } from 'src/app/model/bet-subject';
import { BetDataType } from 'src/app/model/enum/bet-data-type';


@Component({
  selector: 'app-bet-item-dialog',
  templateUrl: './bet-item-dialog.component.html',
  styleUrls: ['./bet-item-dialog.component.scss']
})
export class BetItemDialogComponent implements OnInit {
  betItem: BetItem | undefined
  betItemOpen = false

  constructor(private betService: BetService,
              @Inject(MAT_DIALOG_DATA) public betItemData: BetItemData,
              private dialog: MatDialog) { }

  ngOnInit(): void {
    this.getData();
  }

  getData() {
    this.betService.getBetItem(this.betItemData.betId, this.betItemData.type).subscribe({
      next: (data) => {
        this.betItem = data;
        console.log(this.betItem.status)
        this.betItemOpen = this.betItem.status == BetItemStatus.OPEN
      }
    })
  }


  openDriverDialog(index: number) {
    this.openBetSubjectDialog(index, BetSubjectType.DRIVER);
  }

  openConstructorDialog(index: number) {
    this.openBetSubjectDialog(index, BetSubjectType.CONSTRUCTOR);
  }

  openBetSubjectDialog(index: number, betSubjectType: BetSubjectType) {
    let excludeBetSubjects: number[] = [];

    this.betItem?.positions.forEach(position => {
      if (position.betSubject.id) {
        excludeBetSubjects.push(position.betSubject.id);
      }
    });

    const dialogRef = this.dialog?.open(BetSubjectComponent, {
      data: {type: betSubjectType, raceId: this.betItem?.raceId, excludeBetSubjects: excludeBetSubjects}
    });

    dialogRef?.afterClosed().subscribe((betSubject: BetSubject) => {
      if (!betSubject) {
        return
      }
      if (this.betItem) {
        this.betItem.positions[index].betSubject = betSubject;
        console.log(this.betItem.positions)
      }
    });
  }

  setFastestLap(index: number) {
    if (this.betItem?.type != BetDataType.RACE 
      || !this.betItem.positions[index].betSubject.name) return

    if (this.betItem.positions[index].fastestLap) {
      this.betItem.positions[index].fastestLap = false;
    } else {
      this.betItem.positions.forEach(position => {
        position.fastestLap = false;
      })
      this.betItem.positions[index].fastestLap = true;
    }      
  }
  
}
