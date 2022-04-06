import { SafeKeyedRead } from "@angular/compiler";
import { emptyLeague, League } from "./league";
import { LoginData } from "./login-data";

export class UserData {
    selectedLanguage: string;
    selectedLeague: League;
    leagues: League[];
    loginData: LoginData | undefined

    constructor(
        loginData = undefined, 
        selectedLanguage = '', 
        selectedLeague = emptyLeague(), 
        leagues: League[] = []) {

            this.selectedLanguage = selectedLanguage;
            this.selectedLeague = selectedLeague;
            this.loginData = loginData;
            this.leagues = leagues;
    }
}