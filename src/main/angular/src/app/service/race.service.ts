import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Bet } from '../model/bet';
import { LeagueService } from './league.service';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class RaceService {

  constructor(
    private http: HttpClient, 
    private userService: UserService,
    private leagueService: LeagueService) { }

  public getBet(raceId: string): Observable<Bet> {
    const headers = this.userService.getAuthHeader();
    const leagueId = this.userService.getUserData().selectedLeague.id
    let params = new HttpParams();
    
    params.append('raceId', raceId)
    params.append('leagueId', leagueId)
    
    return this.http.get<Bet>(
      `${environment.apiServerUrl}/race/bet?raceId=${raceId}&leagueId=${leagueId}`,
      { headers }
    );    
  }
}
