import { Component, Input, OnInit } from '@angular/core';
import { BetItem } from 'src/app/model/bet-item';
import { BetItemData } from 'src/app/model/bet-item-data';
import { BetResult } from 'src/app/model/bet-result';
import { BetDataType } from 'src/app/model/enum/bet-data-type';
import { Position } from 'src/app/model/position';
import { BetService } from 'src/app/service/bet.service';

@Component({
  selector: 'app-bet-results',
  templateUrl: './bet-results.component.html',
  styleUrls: ['./bet-results.component.scss']
})
export class BetResultsComponent implements OnInit {

  @Input()
  betItemData: BetItemData | undefined

  betResult: BetResult | undefined

  largerPositionArray: Position[] = []
  smallerPositionArray: Position[] = []

  constructor(private betService: BetService) { }

  ngOnInit(): void {
    this.getData()
  }

  getData() {
    this.betService.getBetItemResult(this.betItemData?.betId, this.betItemData?.type).subscribe({
      next: (betItemResult) => {
        this.betResult = betItemResult;
      }
    })
  }

}
