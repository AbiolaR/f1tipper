import { Component, OnInit } from '@angular/core';
import { BetService } from '../../service/bet.service';
import { UserService } from 'src/app/service/user.service';
import { League } from 'src/app/model/league';
import { MatDialog } from '@angular/material/dialog';
import { LeagueDialogComponent } from '../../component/dialog/league-dialog/league-dialog.component';
import { AppComponent } from 'src/app/app.component';
import { Bet } from 'src/app/model/bet';
import { UserData } from 'src/app/model/user-data';

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
        this.selectedLeague = league
      }
    }
    this.getBets();    
  }

  ngOnReload(): void {

  }

  private getBets() {
    if (this.selectedLeague) {
      this.betService.getBets(this.selectedLeague?.id).subscribe({
        next: (data) => { this.bets = data; this.app.isLoading = false; }
      })
    } else {
      const dialogRef = this.dialog.open(LeagueDialogComponent)
          dialogRef.afterClosed().subscribe(result => {
            if(result) {
              this.getBets()
            }
          });
    }
  }

  onSelectedLeagueChange(league: League) {
    this.userData!!.selectedLeague = league;
    this.userService.setUserData(this.userData!!);
    this.bets = [];
    this.app.isLoading = true;
    this.getBets();
  }

}
