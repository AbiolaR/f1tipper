import { Component, OnInit, Inject } from '@angular/core';
import { BetService } from '../../../service/bet.service';
import {MAT_DIALOG_DATA, MatDialog} from '@angular/material/dialog';
import { BetItem as BetItem } from 'src/app/model/bet-item';
import { BetDataType, BetItemData } from 'src/app/model/bet-item-data';
import { DriverComponent } from '../../driver/driver.component';
import { Driver } from 'src/app/model/driver';


@Component({
  selector: 'app-bet-item-dialog',
  templateUrl: './bet-item-dialog.component.html',
  styleUrls: ['./bet-item-dialog.component.scss']
})
export class BetItemDialogComponent implements OnInit {
  betItem: BetItem | undefined

  constructor(private betService: BetService,
              @Inject(MAT_DIALOG_DATA) public betItemData: BetItemData,
              private dialog: MatDialog) { }

  ngOnInit(): void {
    this.getData();
  }

  getData() {
    this.betService.getBetItem(this.betItemData.id, this.betItemData.type).subscribe({
      next: (data) => {
        this.betItem = data;
      }
    })
  }


  openDriverDialog(index: number) {
    let excludeDrivers: number[] = [];

    this.betItem?.positions.forEach(position => {
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
      if (this.betItem) {
        this.betItem.positions[index].driver = driver;
      }
      //this.bet?.positions.set(index, driver)
      console.log(this.betItem?.positions)      
    });
  }
}
