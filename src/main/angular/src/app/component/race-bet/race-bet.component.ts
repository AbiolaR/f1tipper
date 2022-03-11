import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'
import { RaceBet } from '../../model/race-bet';
import { RaceBetService } from '../../service/race-bet.service';
import {MatDialog} from '@angular/material/dialog';
import { BetItemData, BetDataType } from 'src/app/model/bet-item-data';
import { BetItemDialogComponent } from '../dialog/bet-item-dialog/bet-item-dialog.component';
import { BetItem } from 'src/app/model/bet-item';

@Component({
  selector: 'app-race-bet',
  templateUrl: './race-bet.component.html',
  styleUrls: ['./race-bet.component.scss']
})
export class RaceBetComponent implements OnInit {

  private raceBetId: string | null | undefined;
  raceBet: RaceBet | undefined;

  constructor(private activatedRoute: ActivatedRoute, 
              private raceBetService: RaceBetService, 
              public dialog: MatDialog ) { }

  ngOnInit(): void {
    this.raceBetId = this.activatedRoute.snapshot.paramMap.get("id");
    this.raceBetService.getRaceBet(this.raceBetId).subscribe({      
      next: (data) => {this.raceBet = data
      console.log(this.raceBet)}
    })
  }

  openQualifyingDialog() {
    const dialogRef = this.dialog.open(BetItemDialogComponent, {
      data: new BetItemData(this.raceBet?.id, BetDataType.QUALIFYING)
    })

    this.handleDialogClose(dialogRef);
  }

  openRaceDialog() {
    const dialogRef = this.dialog.open(BetItemDialogComponent, {    
      data: new BetItemData(this.raceBet?.id, BetDataType.RACE)
    })

    this.handleDialogClose(dialogRef);
  }

  openDNFDialog() {
    const dialogRef = this.dialog.open(BetItemDialogComponent, {    
      data: new BetItemData(this.raceBet?.id, BetDataType.DNF)
    })

    this.handleDialogClose(dialogRef);
  }

  handleDialogClose(dialogRef: any) {
    dialogRef.afterClosed().subscribe((betItem: BetItem) => {
      if (betItem) {
        this.raceBetService.saveBet(betItem).subscribe(betItem => console.log(betItem));
      }
    });
  }

}
