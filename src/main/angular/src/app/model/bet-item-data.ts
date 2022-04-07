import { BetSubject } from "./bet-subject";
import { BetDataType } from "./enum/bet-data-type";


export class BetItemData {
    betId: number | undefined;
    type: BetDataType;
    betSubjects: BetSubject[];

    constructor(betId: number, type: BetDataType, betSubjects: BetSubject[]) {
        this.betId = betId;
        this.type = type;
        this.betSubjects = betSubjects;
    }
}