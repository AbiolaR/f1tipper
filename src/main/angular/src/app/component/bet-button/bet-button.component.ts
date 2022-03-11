import { Component, Input, OnInit } from '@angular/core';
import { Bet } from 'src/app/model/bet';

@Component({
  selector: 'app-bet-button',
  templateUrl: './bet-button.component.html',
  styleUrls: ['./bet-button.component.scss']
})
export class BetButtonComponent implements OnInit {
  
  constructor() { }
  
  @Input()
  bet: Bet | undefined;

  ngOnInit(): void { }

}
