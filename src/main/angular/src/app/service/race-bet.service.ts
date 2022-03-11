import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { BetItem } from '../model/bet-item';
import { RaceBet } from '../model/race-bet';
import { RaceBetListItem } from '../model/race-bet-list-item';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class RaceBetService {

  constructor(private http: HttpClient, private userService: UserService) { }

  /* GET REQUESTS */
  public getRaceBetItems(leagueId: number | undefined): Observable<RaceBetListItem[]> {
    const headers = this.userService.getAuthHeader();
    
    return this.http.get<RaceBetListItem[]>(
      `${environment.apiServerUrl}/bet/leagueitems/${leagueId}`, 
       { headers } 
    );
  }

  public getBetItem(id: number | undefined, type: string): Observable<BetItem> {
    const headers = this.userService.getAuthHeader();
    return this.http.get<BetItem>(
      `${environment.apiServerUrl}/bet/item/${type}/${id}`,
      { headers }
    );
  }

  public getRaceBet(id: string | null): Observable<RaceBet> {
    const headers = this.userService.getAuthHeader();
    return this.http.get<RaceBet>(
      `${environment.apiServerUrl}/bet/racebet/${id}`,
      { headers }
    );    
  }

  /* POST REQUESTS */
  public saveBet(betItem: BetItem): Observable<BetItem> {
    const headers = this.userService.getAuthHeader();
    return this.http.post<BetItem>(
      `${environment.apiServerUrl}/bet/item/save`, betItem,
      { headers }  
    );
  }

  
}
