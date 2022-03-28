import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { BetDataType } from '../model/enum/bet-data-type';
import { BetItemTypeGroup } from '../model/enum/bet-item-type-group';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class ResultService {

  apiUrl = `${environment.apiServerUrl}/result`

  constructor(private http: HttpClient, private userService: UserService) { }

  public triggerResultUpdate(raceId: number, type: BetItemTypeGroup): Observable<boolean> {
    const headers = this.userService.getAuthHeader();
    return this.http.get<boolean>(
      `${this.apiUrl}/${raceId}/${type}/update`, 
        { headers }
      );
  }

  public calculatePoints(raceId: number, type: BetItemTypeGroup): Observable<boolean> {
    const headers = this.userService.getAuthHeader();
    return this.http.get<boolean>(
        `${this.apiUrl}/${raceId}/${type}/calculate`, 
        { headers }
      );
  }
}
