import { Component, OnInit } from '@angular/core';
import { BetService } from '../../service/bet.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import { UserService } from 'src/app/service/user.service';
import { League } from 'src/app/model/league';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { LeagueDialogComponent } from '../../component/dialog/league-dialog/league-dialog.component';
import { AppComponent } from 'src/app/app.component';
import { Bet } from 'src/app/model/bet';
import { LeagueService } from 'src/app/service/league.service';
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

  constructor(private betService: BetService, private _snackBar: MatSnackBar, 
    private userService: UserService, private dialog: MatDialog, 
    public app: AppComponent, private leagueService: LeagueService) { }

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

  /*private getLeagues() {
    this.userService.getUser().subscribe({
      next: (user) => { 
        this.leagues = user.leagues;
        this.selectedLeague = this.getLeague(this.leagues);
        if (this.selectedLeague) {
          this.getBets();      
        } else {
          const dialogRef = this.dialog.open(LeagueDialogComponent)
          dialogRef.afterClosed().subscribe(result => {
            if(result) {
              this.getLeagues()
            }
          });
        }        
      }
    })
  }*/

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

  /*private getLeague(leagues: League[]): League {
    const locallySelectedLeague = this.leagueService.getLocalSelectedLeague()
    if (locallySelectedLeague) {
      for(let league of leagues) {
        if (league.name == locallySelectedLeague.name ) {
          return league
        }
      }
    }
    this.onSelectedLeagueChange(leagues[0])
    return leagues[0]
  }*/

  onSelectedLeagueChange(league: League) {
    this.userData!!.selectedLeague = league;
    this.userService.setUserData(this.userData!!);
  }

}
