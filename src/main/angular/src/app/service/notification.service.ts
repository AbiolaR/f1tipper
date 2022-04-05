import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { PushSubscriber } from '../model/push-subscriber';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private http: HttpClient, private userService: UserService) { }

  public savePushSubscriber(pushSubscriber: PushSubscriber): Observable<Boolean> {
    const headers = this.userService.getAuthHeader();
    return this.http.post<Boolean>(
      `${environment.apiServerUrl}/notification/save`, pushSubscriber,
      { headers }  
    );
  }
}
