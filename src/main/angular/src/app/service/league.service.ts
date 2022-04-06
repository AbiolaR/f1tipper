import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { League } from '../model/league';
import { LeagueOverview } from '../model/league-overview';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class LeagueService {

  apiUrl = `${environment.apiServerUrl}/league`

  constructor(private http: HttpClient, private userService: UserService) { }

  public getLeagueStandings(leagueId: number): Observable<LeagueOverview> {
    const headers = this.userService.getAuthHeader();
    return this.http.get<LeagueOverview>(`${this.apiUrl}/${leagueId}/standings`, { headers })
  }

  public getLeagueStatistics(leagueId: number): Observable<Map<string, string>> {
    const headers = this.userService.getAuthHeader();
    return this.http.get<Map<string, string>>(`${this.apiUrl}/${leagueId}/statistics`, { headers })
  }

  public joinLeague(leagueName: string): Observable<boolean> {
    const headers = this.userService.getAuthHeader();

    return this.http.get<boolean>(`${this.apiUrl}/join/${leagueName}`, { headers });
  }

}
