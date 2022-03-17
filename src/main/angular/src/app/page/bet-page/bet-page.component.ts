import { Component, OnInit } from '@angular/core';
import { BetService } from '../../service/bet.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import { UserService } from 'src/app/service/user.service';
import { League } from 'src/app/model/league';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { LeagueDialogComponent } from '../../component/dialog/league-dialog/league-dialog.component';
import { AppComponent } from 'src/app/app.component';
import { Bet } from 'src/app/model/bet';
import { getMultipleValuesInSingleSelectionError } from '@angular/cdk/collections';
import { LeagueService } from 'src/app/service/league.service';

@Component({
  selector: 'app-bet-page',
  templateUrl: './bet-page.component.html',
  styleUrls: ['./bet-page.component.scss']
})
export class BetPageComponent implements OnInit {

  bets: Bet[] = [];
  leagues: League[] = [];
  selectedLeague: League  | undefined

  constructor(private betService: BetService, private _snackBar: MatSnackBar, 
    private userService: UserService, private dialog: MatDialog, 
    public app: AppComponent, private leagueService: LeagueService) { }

  ngOnInit(): void {
    this.app.isLoading = true;
    this.getLeagues();    
  }

  ngOnReload(): void {

  }

  private getLeagues() {
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
        this.app.isLoading = false;
      }
    })
  }

  private getBets() {
    this.betService.getBets(this.selectedLeague?.id).subscribe({
      next: (data) => { this.bets = data; console.log(this.bets) }
    })
  }

  private getLeague(leagues: League[]): League {
    const locallySelectedLeague = this.leagueService.getLocalSelectedLeague
    if (!locallySelectedLeague) {
      if (leagues.includes(locallySelectedLeague)) {
        return locallySelectedLeague
      }
    }
    return leagues[0]
  }

  onSelectedLeagueChange() {
    this.getBets()
  }

}
