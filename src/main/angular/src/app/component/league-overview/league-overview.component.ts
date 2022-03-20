import { KeyValue } from '@angular/common';
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

  keyDescOrder = (a: KeyValue<number,string>, b: KeyValue<number,string>): number => {
    return +a.key > +b.key ? -1 : (+b.key > +a.key ? 1 : 0);
  }

}
