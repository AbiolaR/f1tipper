import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'
import { Bet } from '../../model/bet';
import { BetService } from '../../service/bet.service';
import { MatDialog } from '@angular/material/dialog';
import { BetItemData } from 'src/app/model/bet-item-data';
import { BetItemDialogComponent } from '../dialog/bet-item-dialog/bet-item-dialog.component';
import { BetItem } from 'src/app/model/bet-item';
import { BetDataType } from 'src/app/model/enum/bet-data-type';
import { UserService } from 'src/app/service/user.service';
import { BetItemTypeGroup } from 'src/app/model/enum/bet-item-type-group';

@Component({
  selector: 'app-bet',
  templateUrl: './bet.component.html',
  styleUrls: ['./bet.component.scss']
})
export class BetComponent implements OnInit {

  BetItemTypeGroup = BetItemTypeGroup
  BetDataType = BetDataType
  isAdmin = false
  private betId: string | null | undefined;

  @Input()
  bet: Bet | undefined;

  constructor(private activatedRoute: ActivatedRoute, 
              private betService: BetService, 
              public dialog: MatDialog,
              private userService: UserService) { }

  ngOnInit(): void {
    this.isAdmin = this.userService.isAdmin()
    if (!this.bet) {
      this.betId = this.activatedRoute.snapshot.paramMap.get("id");
      this.betService.getBet(this.betId).subscribe({      
        next: (data) => this.bet = data
      })
    }
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

}
