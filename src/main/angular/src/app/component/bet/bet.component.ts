import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'
import { Bet } from '../../model/bet';
import { BetService } from '../../service/bet.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { BetItemData } from 'src/app/model/bet-item-data';
import { BetItemDialogComponent } from '../dialog/bet-item-dialog/bet-item-dialog.component';
import { BetItem } from 'src/app/model/bet-item';
import { BetDataType } from 'src/app/model/enum/bet-data-type';
import { UserService } from 'src/app/service/user.service';
import { BetItemTypeGroup } from 'src/app/model/enum/bet-item-type-group';
import { BetSubjectType } from 'src/app/model/enum/bet-subject-type';
import { BetSubject } from 'src/app/model/bet-subject';
import { BetSubjectService } from 'src/app/service/bet-subject.service';
import { BetItemStatus } from 'src/app/model/enum/bet-item-status';
import { DateTime, Duration } from 'luxon';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-bet',
  templateUrl: './bet.component.html',
  styleUrls: ['./bet.component.scss']
})
export class BetComponent implements OnInit {
  dialogRef: MatDialogRef<BetItemDialogComponent, any> | undefined
  BetItemTypeGroup = BetItemTypeGroup
  BetDataType = BetDataType
  dateMessage = ''
  isAdmin = false
  private betId: string | null | undefined;
  private betSubjectMap = new Map<BetSubjectType, BetSubject[]>()

  @Input()
  bet: Bet | undefined;

  constructor(private activatedRoute: ActivatedRoute, 
              private betService: BetService, 
              public dialog: MatDialog,
              private userService: UserService,
              private betSubjectService: BetSubjectService,
              private translate: TranslateService) { }

  ngOnInit(): void {
    this.isAdmin = this.userService.isAdmin()
    if (!this.bet) {
      this.betId = this.activatedRoute.snapshot.paramMap.get("id");
      this.betService.getBet(this.betId).subscribe({      
        next: (data) => {
          this.bet = data;
          if (this.bet.status == BetItemStatus.OPEN) {
            this.setDateMessage();
            this.getBetSubjects();
          }
        }
      })
    }
  }

  setDateMessage() {
    if (!this.bet) { 
      return;
    }
    const qualiStartDate = DateTime.fromMillis(this.bet.race.qualiStartDatetime)
    const raceStartDate = DateTime.fromMillis(this.bet.race.raceStartDatetime)
    let upcomingEvent = '';
    let date = '';
    
    if (this.isWithinThreeDays(qualiStartDate.diffNow())) {
      date = qualiStartDate.setLocale(this.translate.currentLang)
              .toLocaleString(DateTime.DATETIME_MED);
      upcomingEvent = BetDataType.QUALIFYING;
    } else if (this.isWithinThreeDays(raceStartDate.diffNow())) {
      date = raceStartDate.setLocale(this.translate.currentLang)
              .toLocaleString(DateTime.DATETIME_MED);        
      upcomingEvent = BetDataType.RACE;
    }    
    if (upcomingEvent) {
      this.dateMessage = `${this.translate.instant(upcomingEvent)} ${this.translate.instant('begins')}: ${date}`;
    }
    
  }

  private isWithinThreeDays(duration: Duration): Boolean {
    if (duration.as('milliseconds') > 0 && duration.as('days') < 3) {
      return true;
    }
    return false;
  }

  getBetSubjects() {
    this.betSubjectService.getBetSubjects(BetSubjectType.DRIVER, this.bet!!.race.id)
    .subscribe(betSubjects => {
      this.betSubjectMap.set(BetSubjectType.DRIVER, betSubjects);
      this.updateBetSubjects();
    })

    if (this.bet?.type.toUpperCase() == BetItemTypeGroup.CHAMPIONSHIP) {
      this.betSubjectService.getBetSubjects(BetSubjectType.CONSTRUCTOR, this.bet!!.race.id)
        .subscribe(betSubjects => {
          this.betSubjectMap.set(BetSubjectType.CONSTRUCTOR, betSubjects);
          this.updateBetSubjects();
        })
    }
  }

  updateBetSubjects() {
    if (this.dialogRef) {
      if (!this.dialogRef.componentInstance.betItemData.betSubjects.length) {
        const type = this.dialogRef.componentInstance.betItemData.type;
        this.dialogRef.componentInstance.updateBetSubjects(this.getBetSubjectsByType(type));
      }
    }
  }

  getBetSubjectsByType(type: BetDataType): BetSubject[] {
    if (type.toUpperCase() == BetSubjectType.CONSTRUCTOR.valueOf()) {
      return this.betSubjectMap.get(BetSubjectType.CONSTRUCTOR) || []
    } else {
      return this.betSubjectMap.get(BetSubjectType.DRIVER) || []
    }
  }

  openBetItemDialog(type: BetDataType) {
    const betSubjects = this.getBetSubjectsByType(type);

    this.dialogRef = this.dialog.open(BetItemDialogComponent, {
      data: new BetItemData(this.bet!!.id, type, betSubjects),
      width: '100%',
      disableClose: true
    })    
    
    this.handleDialogClose(this.dialogRef);    
  }

  handleDialogClose(dialogRef: any) {
    dialogRef.afterClosed().subscribe((betItem: BetItem) => {
      if (betItem) {
        this.betService.saveBetItem(betItem).subscribe();
      }
    });
  }

}
