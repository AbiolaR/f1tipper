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
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSpinner } from '@angular/material/progress-spinner';
import { BetItemTypeGroup } from 'src/app/model/enum/bet-item-type-group';

@Component({
  selector: 'app-bet',
  templateUrl: './bet.component.html',
  styleUrls: ['./bet.component.scss']
})
export class BetComponent implements OnInit {

  BetItemTypeGroup = BetItemTypeGroup
  isAdmin = false
  private betId: string | null | undefined;
  bet: Bet | undefined;

  constructor(private activatedRoute: ActivatedRoute, 
              private betService: BetService, 
              public dialog: MatDialog,
              private resultService: ResultService,
              private userService: UserService,
              private snackBar: MatSnackBar ) { }

  ngOnInit(): void {
    this.isAdmin = this.userService.isAdmin()
    this.betId = this.activatedRoute.snapshot.paramMap.get("id");
    this.betService.getBet(this.betId).subscribe({      
      next: (data) => {this.bet = data
      console.log(this.bet)}
    })
  }

  openQualifyingDialog() {
    this.openBetItemDialog(BetDataType.QUALIFYING);
  }

  openRaceDialog() {
    this.openBetItemDialog(BetDataType.RACE);
  }

  openDNFDialog() {
    this.openBetItemDialog(BetDataType.DNF);
  }

  openDriverDialog() {
    this.openBetItemDialog(BetDataType.DRIVER);
  }

  openConstructorDialog() {
    this.openBetItemDialog(BetDataType.CONSTRUCTOR);
  }

  openBetItemDialog(type: BetDataType) {
    const dialogRef = this.dialog.open(BetItemDialogComponent, {
      data: new BetItemData(this.bet?.id, type),
      width: '100%',
      disableClose: true
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

  /*updateResult(typeGroup: BetItemTypeGroup, button: any) {
    button.classList.add('loading');
    this.resultService.triggerResultUpdate(this.bet!!.raceId, typeGroup).subscribe(result => {
      button.classList.remove('loading');
      this.handleResult(result);
    });
  }

  handleResult(result: boolean) {
    let message = 'oh oh, something went wrong';
    let action = '';
    if (result) {
      message = 'successful';
      action = 'reload'
    }
    let snackBarRef = this.snackBar.open(message, action, {duration: 2500, verticalPosition: 'bottom'});
    snackBarRef.onAction().subscribe(() => {
      window.location.reload();
    });
  }

  calculatePoints(typeGroup: BetItemTypeGroup) {
    this.resultService.calculatePoints(this.bet!!.raceId, typeGroup).subscribe(result => {
      this.handleResult(result);
    });
  }*/

}
