import { Component, Input, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BetItemTypeGroup } from 'src/app/model/enum/bet-item-type-group';
import { ResultService } from 'src/app/service/result.service';

@Component({
  selector: 'app-admin-control-button [typeGroup] [raceId]',
  templateUrl: './admin-control-button.component.html',
  styleUrls: ['./admin-control-button.component.scss']
})
export class AdminControlButtonComponent implements OnInit {

  buttonText: string = ""

  @Input()
  typeGroup: BetItemTypeGroup | undefined

  @Input()
  raceId: number = 0

  constructor(private resultService: ResultService, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    switch(this.typeGroup) {
      case BetItemTypeGroup.RACE:
        this.buttonText = "Race";
        break;
      case BetItemTypeGroup.QUALIFYING:
        this.buttonText = "Qualifying";
        break;
      case BetItemTypeGroup.CHAMPIONSHIP:
        this.buttonText = "Championship";
        break;
    }
  }

  updateResult(button: any) {
    button.classList.add('loading');
    this.resultService.triggerResultUpdate(this.raceId, this.typeGroup!!).subscribe(result => {
      button.classList.remove('loading');
      this.handleResult(result);
    });
  }

  calculatePoints() {
    this.resultService.calculatePoints(this.raceId, this.typeGroup!!).subscribe(result => {
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

}
