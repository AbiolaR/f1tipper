import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UserService } from './user.service';
import { BetSubjectType } from '../model/enum/bet-subject-type';
import { BetSubject } from '../model/bet-subject';

@Injectable({
  providedIn: 'root'
})
export class BetSubjectService {
  apiUrl = `${environment.apiServerUrl}/betsubject`

  constructor(private http: HttpClient, private userService: UserService) { }

  public getBetSubjects(type: BetSubjectType, raceId: number): Observable<BetSubject[]> {
    const headers = this.userService.getAuthHeader();
    return this.http.get<BetSubject[]>(`${environment.apiServerUrl}/race/${raceId}/${type}`, { headers });
  }
}
