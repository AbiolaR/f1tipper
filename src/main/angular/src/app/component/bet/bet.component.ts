import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'
import { Bet } from '../../model/bet';
import { BetService } from '../../service/bet.service';
import {MatDialog} from '@angular/material/dialog';
import { BetItemData } from 'src/app/model/bet-item-data';
import { BetItemDialogComponent } from '../dialog/bet-item-dialog/bet-item-dialog.component';
import { BetItem } from 'src/app/model/bet-item';
import { BetDataType } from 'src/app/model/enum/bet-data-type';

@Component({
  selector: 'app-bet',
  templateUrl: './bet.component.html',
  styleUrls: ['./bet.component.scss']
})
export class BetComponent implements OnInit {

  private betId: string | null | undefined;
  bet: Bet | undefined;

  constructor(private activatedRoute: ActivatedRoute, 
              private betService: BetService, 
              public dialog: MatDialog ) { }

  ngOnInit(): void {
    this.betId = this.activatedRoute.snapshot.paramMap.get("id");
    this.betService.getBet(this.betId).subscribe({      
      next: (data) => {this.bet = data
      console.log(this.bet)}
    })
  }

  openQualifyingDialog() {
    const dialogRef = this.dialog.open(BetItemDialogComponent, {
      data: new BetItemData(this.bet?.id, BetDataType.QUALIFYING)
    })

    this.handleDialogClose(dialogRef);
  }

  openRaceDialog() {
    const dialogRef = this.dialog.open(BetItemDialogComponent, {    
      data: new BetItemData(this.bet?.id, BetDataType.RACE)
    })

    this.handleDialogClose(dialogRef);
  }

  openDNFDialog() {
    const dialogRef = this.dialog.open(BetItemDialogComponent, {    
      data: new BetItemData(this.bet?.id, BetDataType.DNF)
    })

    this.handleDialogClose(dialogRef);
  }

  openDriverDialog() {
    const dialogRef = this.dialog.open(BetItemDialogComponent, {
      data: new BetItemData(this.bet?.id, BetDataType.DRIVER)
    })
    this.handleDialogClose(dialogRef);
  }

  openConstructorDialog() {
    const dialogRef = this.dialog.open(BetItemDialogComponent, {
      data: new BetItemData(this.bet?.id, BetDataType.CONSTRUCTOR)
    })
    this.handleDialogClose(dialogRef);
  }

  handleDialogClose(dialogRef: any) {
    dialogRef.afterClosed().subscribe((betItem: BetItem) => {
      if (betItem) {
        this.betService.saveBetItem(betItem).subscribe(betItem => console.log(betItem));
      }
    });
  }

}
