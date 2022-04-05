import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LeagueService } from 'src/app/service/league.service';
import { RaceService } from 'src/app/service/race.service';

@Component({
  selector: 'app-race-to-bet-redirect',
  templateUrl: './race-to-bet-redirect.component.html',
  styleUrls: ['./race-to-bet-redirect.component.scss']
})
export class RaceToBetRedirectComponent implements OnInit {

  raceId = ''

  constructor(
    private raceService: RaceService,    
    private activatedRoute: ActivatedRoute, 
    private router: Router) { }

  ngOnInit(): void {
    this.raceId = this.activatedRoute.snapshot.paramMap.get("id") || '';
    console.info(`race id is: ${this.raceId}`)
    this.raceService.getBet(this.raceId).subscribe({      
      next: (bet) => {console.log(bet); this.router.navigate([`bet/${bet.id}`, { bet: bet }])}
    })
  }

}
