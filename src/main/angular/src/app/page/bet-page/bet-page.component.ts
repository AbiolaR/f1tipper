import { Component, OnInit } from '@angular/core';
import { BetService } from '../../service/bet.service';
import { UserService } from 'src/app/service/user.service';
import { League } from 'src/app/model/league';
import { MatDialog } from '@angular/material/dialog';
import { LeagueDialogComponent } from '../../component/dialog/league-dialog/league-dialog.component';
import { AppComponent } from 'src/app/app.component';
import { Bet } from 'src/app/model/bet';
import { UserData } from 'src/app/model/user-data';
import { BetData } from 'src/app/model/bet-data';
import { BetItemStatus } from 'src/app/model/enum/bet-item-status';

@Component({
  selector: 'app-bet-page',
  templateUrl: './bet-page.component.html',
  styleUrls: ['./bet-page.component.scss']
})
export class BetPageComponent implements OnInit {

  bets: Bet[] = [];
  selectedLeague: League  | undefined
  userData: UserData | undefined;

  constructor(
    private betService: BetService,
    private userService: UserService, 
    private dialog: MatDialog, 
    public app: AppComponent) { }

  ngOnInit(): void {
    this.app.isLoading = true;
    this.userData = this.userService.getUserData();
    for (let league of this.userData.leagues) {
      if (league.name == this.userData.selectedLeague.name ) {
        this.selectedLeague = league;
        break;
      }
    }
    this.getBets();    
  }



  ngAfterViewInit(): void {
    if (this.bets.length != 0) {
      let firstRace = this.bets.find(bet => bet.status == BetItemStatus.OPEN);
      const firstRaceElement = document.querySelectorAll(`[ng-reflect-router-link="/bet/${firstRace?.id}"]`).item(0);
      firstRaceElement.scrollIntoView({behavior:"smooth"});
    }   
  }

  private getBets() {
    if (this.selectedLeague) {
      const betData = this.userData!!.betData;
      if (!betData) {
        this.updateBets();
        return;
      }
      if(isNaN(betData.lastUpdate)) {
        betData.lastUpdate = 1;
      }
      const dataAgeInMinutes = (new Date().valueOf() - betData.lastUpdate) / 60000
      if (!betData.bets.length ||  dataAgeInMinutes > 30) {
        this.updateBets();
      } else {
        this.bets = betData.bets;
        this.app.isLoading = false;
      }
    } else {
      const dialogRef = this.dialog.open(LeagueDialogComponent)
          dialogRef.afterClosed().subscribe(result => {
            if(result) {
              this.getBets();
            }
          });
    }
  }

  private updateBets() {
    this.betService.getBets(this.selectedLeague?.id).subscribe({
      next: (data) => { 
        this.bets = data;
        this.app.isLoading = false;
        this.userData!!.betData = new BetData(data);
        this.userService.setUserData(this.userData!!);
      }
    })
  }

  onSelectedLeagueChange(league: League) {
    this.userData!!.selectedLeague = league;
    this.userService.setUserData(this.userData!!);
    this.bets = [];
    this.app.isLoading = true;
    this.getBets();
  }

}
