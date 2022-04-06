import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { League } from 'src/app/model/league';
import { UserData } from 'src/app/model/user-data';
import { LeagueService } from 'src/app/service/league.service';
import { UserService } from 'src/app/service/user.service';
import { UserDialogComponent } from '../dialog/user-dialog/user-dialog.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  userData: UserData | undefined;
  selectedLeague: League  | undefined

  constructor(
    private userService: UserService, 
    private leagueService: LeagueService,
    private dialog: MatDialog) {}

  ngOnInit(): void {
    this.userData = this.userService.getUserData();
    for (let league of this.userData.leagues) {
      if (league.name == this.userData.selectedLeague.name ) {
        this.selectedLeague = league
      }
    } 
  }

  onSelectedLeagueChange(league: League) {
    if (this.userData) {
      this.userData.selectedLeague = league
      this.userService.setUserData(this.userData)
    }
  }

  openUserDialog() {
    this.dialog.open(UserDialogComponent, {
      width: '100%',
      autoFocus: false,
      data: this.userData
    })
  }

}