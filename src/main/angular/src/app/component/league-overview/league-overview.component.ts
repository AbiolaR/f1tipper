import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { LeagueOverview } from 'src/app/model/league-overview';
import { LeagueService } from 'src/app/service/league.service';

@Component({
  selector: 'app-league-overview',
  templateUrl: './league-overview.component.html',
  styleUrls: ['./league-overview.component.scss']
})
export class LeagueOverviewComponent implements OnInit {

  @Input()
  leagueId: number = 0



  leagueOverview: LeagueOverview | undefined

  constructor(private leagueService: LeagueService) { }

  ngOnInit(): void {
    this.getLeagueOverview()
  }

  ngOnChanges(changes: SimpleChanges) {
    this.getLeagueOverview()
  }

  getLeagueOverview() {
    if (this.leagueId) {
      this.leagueService.getLeagueOverview(this.leagueId).subscribe({
        next: (data) => {
          this.leagueOverview = data;
        }
      });
    }
  }

  keepOrder() {
    return 1;
  }

}
