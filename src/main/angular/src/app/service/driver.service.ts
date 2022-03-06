import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Driver } from '../model/driver';
import { environment } from 'src/environments/environment';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class DriverService {
  constructor(private http: HttpClient, private userService: UserService) { }

  public getDrivers(): Observable<Driver[]> {
    const headers = this.userService.getAuthHeader();
    return this.http.get<Driver[]>(`${environment.apiServerUrl}/driver/all`, { headers });
  }
}
