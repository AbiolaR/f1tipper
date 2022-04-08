import { BetData } from "./bet-data";
import { emptyLeague, League } from "./league";
import { LoginData } from "./login-data";

export class UserData {
    [key: string]: any;
    selectedLanguage: string;
    selectedLeague: League;
    leagues: League[];
    loginData: LoginData | undefined;
    betData: BetData;

    constructor(
        loginData = undefined, 
        selectedLanguage = '', 
        selectedLeague = emptyLeague(), 
        leagues: League[] = [],
        betData = new BetData()) {

            this.selectedLanguage = selectedLanguage;
            this.selectedLeague = selectedLeague;
            this.loginData = loginData;
            this.leagues = leagues;
            this.betData = betData;
    }
}