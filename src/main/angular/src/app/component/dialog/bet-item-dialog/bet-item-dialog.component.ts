import { Component, OnInit, Inject } from '@angular/core';
import { BetService } from '../../../service/bet.service';
import {MAT_DIALOG_DATA, MatDialog} from '@angular/material/dialog';
import { BetItem as BetItem } from 'src/app/model/bet-item';
import { BetItemData } from 'src/app/model/bet-item-data';
import { BetSubjectComponent } from '../../bet-subject/bet-subject.component';
import { BetItemStatus } from 'src/app/model/enum/bet-item-status';
import { BetSubjectType } from 'src/app/model/enum/bet-subject-type';
import { BetSubject, EmptyBetSubject } from 'src/app/model/bet-subject';
import { BetDataType } from 'src/app/model/enum/bet-data-type';

@Component({
  selector: 'app-bet-item-dialog',
  templateUrl: './bet-item-dialog.component.html',
  styleUrls: ['./bet-item-dialog.component.scss'],
})
export class BetItemDialogComponent implements OnInit {
  animationState: string = '';
  betItem: BetItem | undefined
  betItemOpen = false
  lastPosX = 0;
  lastPosY = 0;

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
        if(!betSubject.name) this.betItem.positions[index].fastestLap = false
        this.betItem.positions[index].betSubject = betSubject;
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

  setToEmpty(event: any, index: number) {
    if (this.betItem!!.positions[index].betSubject.type == BetSubjectType.UNKNOWN) {
      return;
    }
    let element = event.target;

    let posX = event.deltaX + this.lastPosX;
    let posY = event.deltaY + this.lastPosY;
    element.style.left = posX + "px";

    if (event.isFinal) {      
      element.style.left = this.lastPosX + "px";

      let xDiff = Math.abs(this.lastPosX - posX)
      let yDiff = Math.abs(this.lastPosY - posY)
      if (yDiff < xDiff && xDiff >= 35) {
        //console.warn(Math.abs(this.lastPosX - posX))
        this.betItem!!.positions[index].betSubject = new EmptyBetSubject();
        this.betItem!!.positions[index].fastestLap = false;
      }
      this.lastPosX = 0;
      this.lastPosY = 0;
    }
  }

  
}
