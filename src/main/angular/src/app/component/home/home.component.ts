import { Component, OnInit } from '@angular/core';
import { MatSelectChange } from '@angular/material/select';
import { League } from 'src/app/model/league';
import { User } from 'src/app/model/user';
import { LeagueService } from 'src/app/service/league.service';
import { UserService } from 'src/app/service/user.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  username!: String;
  leagues: League[] = [];
  selectedLeague: League  | undefined

  constructor(private userService: UserService, private leagueService: LeagueService) { }

  ngOnInit(): void {
    this.getLeagues();
    const user: User = this.userService.getLocalUser();
    this.username = user.username;
    console.log(user);
  }

  private getLeagues() {
    this.userService.getUser().subscribe({
      next: (user) => { 
        this.leagues = user.leagues;
        this.selectedLeague = this.getLeague(this.leagues);
      }
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

  onSelectedLeagueChange(league: League) {
    this.leagueService.saveLocalSelectedLeague(league)
    this.selectedLeague = league
    console.log(this.selectedLeague)
  }

}
