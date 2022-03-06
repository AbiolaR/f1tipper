import { Component, Input, OnInit } from '@angular/core';
import { RaceBetListItem } from '../../model/race-bet-list-item';

@Component({
  selector: 'app-race-bet-item',
  templateUrl: './race-bet-item.component.html',
  styleUrls: ['./race-bet-item.component.scss']
})
export class RaceBetItemComponent implements OnInit {
  
  constructor() { }
  
  @Input()
  raceBetItem: RaceBetListItem | undefined;

  ngOnInit(): void { }

}
