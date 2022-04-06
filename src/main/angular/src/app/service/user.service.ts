import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Token } from "@angular/compiler/src/ml_parser/tokens";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { League } from "../model/league";
import { LoginData } from "../model/login-data";
import { User } from "../model/user";
import { UserData } from "../model/user-data";

@Injectable({
    providedIn: 'root'
})
export class UserService {
    isAuthenticated: boolean = false

    constructor(private http: HttpClient, private router: Router) {}

    public login(username: String, password: String): Observable<UserData> {
        const headers = { 'Content-Type': 'application/x-www-form-urlencoded' };
        return this.http.post<UserData>(
            `${environment.apiServerUrl}/login`, 
            `username=${username}&password=${password}`,
            { headers }
        )
    }

    private getLoginData(): LoginData {
        var currentUser = JSON.parse(localStorage.getItem('userData')!);
        try {
            return currentUser.loginData;
        } catch (error) {            
            this.router.navigateByUrl('/login');        
        }
        return currentUser.userData;
    }

    public isAdmin(): boolean {
        let roles = this.getLoginData().roles

        return roles?.includes("FORMULA_ADMIN")
    }

    public getAuthHeader(): any {
        return { 'Authorization': `Bearer ${this.getLoginData().access_token}` }
    }

    /*public getLocalUser(): User {
        try {
            return { username: JSON.parse(localStorage.getItem('currentUser')!).username, leagues: []};
        } catch (error) {
            this.router.navigateByUrl('/login');
            throw new Error("not logged in");
        }
    }*/

    public getUserData(): UserData {
        try {
            let userData: UserData = JSON.parse(localStorage.getItem('userData')!);
            if (!userData) {
                throw new Error();
            }
            return userData;
        } catch (error) {
            this.router.navigateByUrl('/login');
            throw new Error("not logged in");
        }
    }

    public setUserData(userData: UserData) {
        localStorage.setItem('userData', JSON.stringify(userData));
    }


    // HTTP GETTERS
    public getUser(): Observable<User> {
        const headers = this.getAuthHeader();
        return this.http.get<User>(`${environment.apiServerUrl}/user`, { headers })
    }
}