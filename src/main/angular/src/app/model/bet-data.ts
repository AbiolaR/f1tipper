import { Bet } from "./bet";

export class BetData {
    bets: Bet[] = [];
    lastUpdate: Date = new Date();

    constructor(bets: Bet[] = [], lastUpdate: Date = new Date()) {
        this.bets = bets;
        this.lastUpdate = lastUpdate;
    }
}