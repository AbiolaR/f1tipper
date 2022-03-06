import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Bet } from '../model/bet';
import { RaceBet } from '../model/race-bet';
import { RaceBetListItem } from '../model/race-bet-list-item';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class RaceBetService {

  constructor(private http: HttpClient, private userService: UserService) { }

  /* GET REQUESTS */
  /*public getRaceBetItems(): Observable<RaceBetListItem[]> {
    //return this.http.get<RaceBetListItem[]>(`${environment.apiServerUrl}api/racebetitems/listitem/user/2`);
    const headers = this.userService.getAuthHeader();
    
    return this.http.get<RaceBetListItem[]>(
      `${environment.apiServerUrl}api/racebetitems/listitem`, 
       { headers } 
    );
  }*/

  public getRaceBetItems(leagueId: number | undefined): Observable<RaceBetListItem[]> {
    const headers = this.userService.getAuthHeader();
    
    return this.http.get<RaceBetListItem[]>(
      `${environment.apiServerUrl}/racebetitems/leagueitems/${leagueId}`, 
       { headers } 
    );
  }

  public getRaceBet(id: string | null): Observable<RaceBet> {
    const headers = this.userService.getAuthHeader();
    return this.http.get<RaceBet>(
      `${environment.apiServerUrl}/racebetitems/racebet/${id}`,
      { headers }
    );    
  }

  public getQualifying(id: number | undefined): Observable<Bet> {
    const headers = this.userService.getAuthHeader();
    return this.http.get<Bet>(
      `${environment.apiServerUrl}/racebetitems/qualifying/${id}`,
      { headers }
    );
  }

  public getDNF(id: number | undefined): Observable<Bet> {
    const headers = this.userService.getAuthHeader();
    return this.http.get<Bet>(
      `${environment.apiServerUrl}/racebetitems/dnf/${id}`,
      { headers }
    );
  }

  public getRace(id: number | undefined): Observable<Bet> {
    const headers = this.userService.getAuthHeader();
    return this.http.get<Bet>(
      `${environment.apiServerUrl}/racebetitems/race/${id}`,
      { headers }  
    );
  }

  /* POST REQUESTS */
  public saveBet(bet: Bet): Observable<Bet> {
    const headers = this.userService.getAuthHeader();
    return this.http.post<Bet>(
      `${environment.apiServerUrl}/racebetitems/save/bet`, bet,
      { headers }  
    );
  }

  
}
