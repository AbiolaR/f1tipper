import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Token } from "@angular/compiler/src/ml_parser/tokens";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { League } from "../model/league";
import { TokenData } from "../model/token-data";
import { User } from "../model/user";

@Injectable({
    providedIn: 'root'
})
export class UserService {
    isAuthenticated: boolean = false

    constructor(private http: HttpClient, private router: Router) {}

    public login(username: String, password: String): Observable<TokenData> {
        const headers = { 'Content-Type': 'application/x-www-form-urlencoded' };
        return this.http.post<TokenData>(
            `${environment.apiServerUrl}/login`, 
            `username=${username}&password=${password}`,
            { headers }
        )
    }

    private getTokenData(): TokenData {
        var currentUser = JSON.parse(localStorage.getItem('currentUser')!);
        try {
            return currentUser.tokenData;
        } catch (error) {            
            this.router.navigateByUrl('/login');        
        }
        return currentUser.tokenData;
    }

    public getAuthHeader(): any {
        return { 'Authorization': `Bearer ${this.getTokenData().access_token}` }
    }

    public getLocalUser(): User {
        try {
            return { username: JSON.parse(localStorage.getItem('currentUser')!).username, leagues: []};
        } catch (error) {
            this.router.navigateByUrl('/login');
            throw new Error("not logged in");
        }
    }

    // HTTP GETTERS
    public getUser(): Observable<User> {
        const headers = this.getAuthHeader();
        return this.http.get<User>(`${environment.apiServerUrl}/user`, { headers })
    }
}