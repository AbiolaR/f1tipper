import { Bet } from "./bet";

export class BetData {
    bets: Bet[] = [];
    lastUpdate: number = new Date().valueOf();

    constructor(bets: Bet[] = [], lastUpdate: number = new Date().valueOf()) {
        this.bets = bets;
        this.lastUpdate = lastUpdate;
    }
}