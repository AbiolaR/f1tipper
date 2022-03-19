import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'
import { Bet } from '../../model/bet';
import { BetService } from '../../service/bet.service';
import {MatDialog} from '@angular/material/dialog';
import { BetItemData } from 'src/app/model/bet-item-data';
import { BetItemDialogComponent } from '../dialog/bet-item-dialog/bet-item-dialog.component';
import { BetItem } from 'src/app/model/bet-item';
import { BetDataType } from 'src/app/model/enum/bet-data-type';
import { ResultService } from 'src/app/service/result.service';
import { BetSubjectType } from 'src/app/model/enum/bet-subject-type';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-bet',
  templateUrl: './bet.component.html',
  styleUrls: ['./bet.component.scss']
})
export class BetComponent implements OnInit {

  isAdmin = false
  private betId: string | null | undefined;
  bet: Bet | undefined;

  constructor(private activatedRoute: ActivatedRoute, 
              private betService: BetService, 
              public dialog: MatDialog,
              private resultService: ResultService,
              private userService: UserService ) { }

  ngOnInit(): void {
    this.isAdmin = this.userService.isAdmin()
    this.betId = this.activatedRoute.snapshot.paramMap.get("id");
    this.betService.getBet(this.betId).subscribe({      
      next: (data) => {this.bet = data
      console.log(this.bet)}
    })
  }

  openQualifyingDialog() {
    const dialogRef = this.dialog.open(BetItemDialogComponent, {
      data: new BetItemData(this.bet?.id, BetDataType.QUALIFYING),
      width: '100%',
    })

    this.handleDialogClose(dialogRef);
  }

  openRaceDialog() {
    const dialogRef = this.dialog.open(BetItemDialogComponent, {    
      data: new BetItemData(this.bet?.id, BetDataType.RACE),
      width: '100%',
    })

    this.handleDialogClose(dialogRef);
  }

  openDNFDialog() {
    const dialogRef = this.dialog.open(BetItemDialogComponent, {    
      data: new BetItemData(this.bet?.id, BetDataType.DNF),
      width: '100%',
    })

    this.handleDialogClose(dialogRef);
  }

  openDriverDialog() {
    const dialogRef = this.dialog.open(BetItemDialogComponent, {
      data: new BetItemData(this.bet?.id, BetDataType.DRIVER),
      width: '100%',
    })
    this.handleDialogClose(dialogRef);
  }

  openConstructorDialog() {
    const dialogRef = this.dialog.open(BetItemDialogComponent, {
      data: new BetItemData(this.bet?.id, BetDataType.CONSTRUCTOR),
      width: '100%',
    })
    this.handleDialogClose(dialogRef);
  }

  handleDialogClose(dialogRef: any) {
    dialogRef.afterClosed().subscribe((betItem: BetItem) => {
      if (betItem) {
        console.log(betItem)
        this.betService.saveBetItem(betItem).subscribe(betItem => console.log(betItem));
      }
    });
  }

  updateQualifyingResult() {
    this.resultService.triggerResultUpdate(this.bet!!.raceId, BetDataType.QUALIFYING).subscribe({});
  }

  updateRaceResult() {
    this.resultService.triggerResultUpdate(this.bet!!.raceId, BetDataType.RACE).subscribe({});
  }

  updateChampionshipResult() {
    this.resultService.triggerResultUpdate(this.bet!!.raceId, BetDataType.CONSTRUCTOR).subscribe({});
  }

}
