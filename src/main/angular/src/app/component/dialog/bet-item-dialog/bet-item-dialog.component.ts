import { Component, OnInit, Inject, ViewChildren, QueryList } from '@angular/core';
import { BetService } from '../../../service/bet.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
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
  dialogRef: MatDialogRef<BetSubjectComponent, any> | undefined
  animationState: string = '';
  betItem: BetItem | undefined
  betItemOpen = false
  lastPosX = 0;
  lastPosY = 0;
  tap = new Hammer.Tap({event: 'singletap'});
  doubleTap = new Hammer.Tap({event: 'doubleTap', taps: 2});
  betSubjectToSwitch = { index: -1, element: undefined as any };

  @ViewChildren('betSubjects') betSubjects: QueryList<any> | undefined;
  

  constructor(private betService: BetService,
              @Inject(MAT_DIALOG_DATA) public betItemData: BetItemData,
              private dialog: MatDialog) { }

  ngOnInit(): void {
    this.getData();    
  }

  ngAfterViewInit() {    
    this.betSubjects?.changes.subscribe(() => {
      const betItemButtons = document.getElementById("positions")?.querySelectorAll("button");
      var event = new CustomEvent("buttonReady");
      betItemButtons?.forEach (element => { 
        element.dispatchEvent(event)
      });
    })
  }

  addTapListener(element: any, index: number) {
    let manager = new Hammer.Manager(element);
    element.setAttribute('index', index)

    manager.add([this.doubleTap, this.tap]);
  
    this.doubleTap.recognizeWith(this.tap);
    this.tap.requireFailure(this.doubleTap);
    var event = new CustomEvent('singleTap');

    manager.on('singletap', (e) => {
      e.target.dispatchEvent(event)
    });
    manager.on('doubleTap', (e) => {
      let i = e.target.getAttribute('index') || ''
      this.switchBetSubject(e.target, parseInt(i));
    });
  }

  switchBetSubject(element: HTMLElement, index: number) {
    if (!this.betItem?.positions[index].betSubject.name) {
      return;
    }
    if (this.betSubjectToSwitch.index == -1) {
      element.classList.add('to-be-switched');
      this.betSubjectToSwitch.index = index;
      this.betSubjectToSwitch.element = element;
    } else {
      let betSubjectA = this.betItem?.positions[index].betSubject;
      let fastestLapA = this.betItem?.positions[index].fastestLap;
      let betSubjectB = this.betItem?.positions[this.betSubjectToSwitch.index].betSubject;
      let fastestLapB = this.betItem?.positions[this.betSubjectToSwitch.index].fastestLap;

      this.betItem!!.positions[this.betSubjectToSwitch.index].betSubject = betSubjectA;
      this.betItem!!.positions[this.betSubjectToSwitch.index].fastestLap = fastestLapA;
      this.betItem!!.positions[index].betSubject = betSubjectB;
      this.betItem!!.positions[index].fastestLap = fastestLapB;
      
      this.setSwitchBetSubjectEmpty();
    }
  }

  private setSwitchBetSubjectEmpty() {
    this.betSubjectToSwitch.element?.classList.remove('to-be-switched');
    this.betSubjectToSwitch.index = -1;
    this.betSubjectToSwitch.element = undefined;
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

    this.setSwitchBetSubjectEmpty();

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
      if (yDiff < xDiff && xDiff >= 35) {
        this.betItem!!.positions[index].betSubject = new EmptyBetSubject();
        this.betItem!!.positions[index].fastestLap = false;
      }
      this.lastPosX = 0;
      this.lastPosY = 0;
    }
  }

  
}
