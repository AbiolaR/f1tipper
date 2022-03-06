import { Component, OnInit, Inject } from '@angular/core';
import { RaceBetService } from '../../../service/race-bet.service';
import {MAT_DIALOG_DATA, MatDialog} from '@angular/material/dialog';
import { Bet } from 'src/app/model/bet';
import { BetDataType, BetData } from 'src/app/model/bet-data';
import { DriverComponent } from '../../driver/driver.component';
import { Driver } from 'src/app/model/driver';


@Component({
  selector: 'app-bet-dialog',
  templateUrl: './bet-dialog.component.html',
  styleUrls: ['./bet-dialog.component.scss']
})
export class BetDialogComponent implements OnInit {
  bet: Bet | undefined

  constructor(private raceBetService: RaceBetService,
              @Inject(MAT_DIALOG_DATA) public betData: BetData,
              private dialog: MatDialog) { }

  ngOnInit(): void {
    this.getData();
  }

  getData() {
    switch (this.betData.type) {
      case BetDataType.RACE:
        this.getRaceData();
        break;
      case BetDataType.QUALIFYING:
        this.getQualifyingData();
        break;
      case BetDataType.DNF:
        this.getDNFData();
        break;
    }
  }


  getRaceData() {
    this.raceBetService.getRace(this.betData.id).subscribe({
      next: (data) => {
        this.bet = data;
      }
    })
  }

  getQualifyingData() {
    this.raceBetService.getQualifying(this.betData.id).subscribe({
      next: (data) => {
        this.bet = data;
      }
    })
  }

  getDNFData() {
    this.raceBetService.getDNF(this.betData.id).subscribe({
      next: (data) => {
        this.bet = data;
      }
    })
  }

  openDriverDialog(index: number) {
    let excludeDrivers: number[] = [];

    this.bet?.positions.forEach(position => {
      if (position.driver.id) {
        excludeDrivers.push(position.driver.id);
      }
    });

    const dialogRef = this.dialog?.open(DriverComponent, {
      data: excludeDrivers
    });

    dialogRef?.afterClosed().subscribe((driver: Driver) => {
      if (!driver) {
        return
      }
      if (this.bet) {
        this.bet.positions[index].driver = driver;
      }
      //this.bet?.positions.set(index, driver)
      console.log(this.bet?.positions)      
    });
  }
}
