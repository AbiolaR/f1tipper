import { Component, OnInit } from '@angular/core';
import { League } from 'src/app/model/league';
import { User } from 'src/app/model/user';
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

  constructor(private userService: UserService) { }

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
        this.selectedLeague = this.leagues[0];
      }
    })
  }

}
