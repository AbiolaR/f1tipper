import { Component, OnInit, Inject } from '@angular/core';
import { BetService } from '../../../service/bet.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { BetItem as BetItem } from 'src/app/model/bet-item';
import { BetItemData } from 'src/app/model/bet-item-data';
import { BetSubjectComponent } from '../../bet-subject/bet-subject.component';
import { BetItemStatus } from 'src/app/model/enum/bet-item-status';
import { BetSubjectType } from 'src/app/model/enum/bet-subject-type';
import { BetSubject, EmptyBetSubject } from 'src/app/model/bet-subject';
import { BetDataType } from 'src/app/model/enum/bet-data-type';
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { Position } from 'src/app/model/position';

@Component({
  selector: 'app-bet-item-dialog',
  templateUrl: './bet-item-dialog.component.html',
  styleUrls: ['./bet-item-dialog.component.scss'],
})
export class BetItemDialogComponent implements OnInit {
  dialogRef: MatDialogRef<BetSubjectComponent, any> | undefined
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
        this.betItemOpen = data.status == BetItemStatus.OPEN
        this.betItem = data;
      }
    })
  }

  updateBetSubjects(betSubjects: BetSubject[]) {
    this.betItemData.betSubjects = betSubjects;
    if (this.dialogRef) {
      this.dialogRef.componentInstance.updateBetSubjects(betSubjects);
    }
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


    this.dialogRef = this.dialog.open(BetSubjectComponent, {
      data: {type: betSubjectType, 
        raceId: this.betItem?.raceId,
        betSubjects: this.betItemData.betSubjects,
        excludeBetSubjects: excludeBetSubjects}
    });

    this.dialogRef.afterClosed().subscribe((betSubject: BetSubject) => {
      if (!betSubject) {
        return
      }
      if (this.betItem) {
        this.betItem.positions[index].fastestLap = false;
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
      if (yDiff < 50 && yDiff < xDiff && xDiff >= 35) {
        this.betItem!!.positions[index].betSubject = new EmptyBetSubject();
        this.betItem!!.positions[index].fastestLap = false;
      }
      this.lastPosX = 0;
      this.lastPosY = 0;
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    this.moveBetSubject(this.betItem!!.positions, event.previousIndex, event.currentIndex)
  }

  private moveBetSubject(positions: Position[], fromIndex: number, toIndex: number): void {
    const from =  this.clamp(fromIndex, positions.length - 1);
    const to = this.clamp(toIndex, positions.length - 1);
  
    if (from === to) {
      return;
    }
  
    const betSubject = positions[from].betSubject;
    const fastestLap = positions[from].fastestLap;

    const delta = to < from ? -1 : 1;
  
    for (let i = from; i !== to; i += delta) {
      positions[i].betSubject = positions[i + delta].betSubject;
      positions[i].fastestLap = positions[i + delta].fastestLap;
    }
  
    positions[to].betSubject = betSubject;
    positions[to].fastestLap = fastestLap;
  }

  private clamp(value: number, max: number): number {
    return Math.max(0, Math.min(max, value));
  }


  
}
