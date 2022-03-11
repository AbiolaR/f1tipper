import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { BetItem } from '../model/bet-item';
import { Bet } from '../model/bet';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class BetService {

  constructor(private http: HttpClient, private userService: UserService) { }

  /* GET REQUESTS */
  public getBets(leagueId: number | undefined): Observable<Bet[]> {
    const headers = this.userService.getAuthHeader();
    
    return this.http.get<Bet[]>(
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

  public getBet(id: string | null): Observable<Bet> {
    const headers = this.userService.getAuthHeader();
    return this.http.get<Bet>(
      `${environment.apiServerUrl}/bet/${id}`,
      { headers }
    );    
  }

  /* POST REQUESTS */
  public saveBetItem(betItem: BetItem): Observable<BetItem> {
    const headers = this.userService.getAuthHeader();
    return this.http.post<BetItem>(
      `${environment.apiServerUrl}/bet/item/save`, betItem,
      { headers }  
    );
  }

  
}
