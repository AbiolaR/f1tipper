import { Component, OnInit } from '@angular/core';
import { RaceBetListItem } from '../../model/race-bet-list-item';
import { RaceBetService } from '../../service/race-bet.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import { UserService } from 'src/app/service/user.service';
import { League } from 'src/app/model/league';
import { MatDialog } from '@angular/material/dialog';
import { LeagueDialogComponent } from '../dialog/league-dialog/league-dialog.component';
import { AppComponent } from 'src/app/app.component';

@Component({
  selector: 'app-race-bets',
  templateUrl: './race-bets.component.html',
  styleUrls: ['./race-bets.component.scss']
})
export class RaceBetsComponent implements OnInit {

  raceBetItems: RaceBetListItem[] = [];
  leagues: League[] = [];
  selectedLeague: League  | undefined

  constructor(private raceBetItemService: RaceBetService, private _snackBar: MatSnackBar, 
    private userService: UserService, private dialog: MatDialog, public app: AppComponent) { }

  ngOnInit(): void {
    this.app.isLoading = true;
    this.getLeagues();    
  }

  private getLeagues() {
    this.userService.getUser().subscribe({
      next: (user) => { 
        this.leagues = user.leagues;
        this.selectedLeague = this.leagues[0];
        if (this.selectedLeague) {
          this.getRaceBetItems();      
        } else {
          this.dialog.open(LeagueDialogComponent)
        }
        this.app.isLoading = false;
      }
    })
  }

  private getRaceBetItems() {
    this.raceBetItemService.getRaceBetItems(this.selectedLeague?.id).subscribe({
      next: (data) => this.raceBetItems = data
    })
  }

}
