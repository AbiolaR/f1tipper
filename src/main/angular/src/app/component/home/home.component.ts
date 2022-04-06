import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSelectChange } from '@angular/material/select';
import { TranslateService } from '@ngx-translate/core';
import { League } from 'src/app/model/league';
import { User } from 'src/app/model/user';
import { UserData } from 'src/app/model/user-data';
import { LeagueService } from 'src/app/service/league.service';
import { UserService } from 'src/app/service/user.service';
import { environment } from 'src/environments/environment';
import { UserDialogComponent } from '../dialog/user-dialog/user-dialog.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  userData: UserData | undefined;
  //username!: String;
  //leagues: League[] = [];
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
    //this.getLeagues();
    //const user: User = this.userService.getLocalUser();
    //this.username = user.username;    
  }

  /*private getLeagues() {
    this.userService.getUser().subscribe({
      next: (user) => { 
        this.leagues = user.leagues;
        this.selectedLeague = this.getLeague(this.leagues);
      }
    })
  }*/

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
    //this.leagueService.saveLocalSelectedLeague(league)
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