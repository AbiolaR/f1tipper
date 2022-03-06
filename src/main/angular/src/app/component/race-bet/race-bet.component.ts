import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'
import { RaceBet } from '../../model/race-bet';
import { RaceBetService } from '../../service/race-bet.service';
import {MatDialog} from '@angular/material/dialog';
import { BetData, BetDataType } from 'src/app/model/bet-data';
import { BetDialogComponent } from '../dialog/bet-dialog/bet-dialog.component';
import { Bet } from 'src/app/model/bet';

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
    const dialogRef = this.dialog.open(BetDialogComponent, {
      data: new BetData(this.raceBet?.id, BetDataType.QUALIFYING)
    })

    this.handleDialogClose(dialogRef);
  }

  openRaceDialog() {
    const dialogRef = this.dialog.open(BetDialogComponent, {    
      data: new BetData(this.raceBet?.id, BetDataType.RACE)
    })

    this.handleDialogClose(dialogRef);
  }

  openDNFDialog() {
    const dialogRef = this.dialog.open(BetDialogComponent, {    
      data: new BetData(this.raceBet?.id, BetDataType.DNF)
    })

    this.handleDialogClose(dialogRef);
  }

  handleDialogClose(dialogRef: any) {
    dialogRef.afterClosed().subscribe((bet: Bet) => {
      if (bet) {
        this.raceBetService.saveBet(bet).subscribe(bet => console.log(bet));
      }
    });
  }

}
