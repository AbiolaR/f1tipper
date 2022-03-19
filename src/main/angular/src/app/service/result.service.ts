import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { BetDataType } from '../model/enum/bet-data-type';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class ResultService {

  constructor(private http: HttpClient, private userService: UserService) { }

  public triggerResultUpdate(raceId: number, type: BetDataType): Observable<boolean> {
    const headers = this.userService.getAuthHeader();
    return this.http.get<boolean>(
        `${environment.apiServerUrl}/result/${raceId}/${type}/update`, 
        { headers }
      );
  }
}
