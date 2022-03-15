import { BetDataType } from "./enum/bet-data-type";


export class BetItemData {
    betId: number | undefined;
    type: BetDataType;

    constructor(betId: number | undefined, type: BetDataType) {
        this.betId = betId;
        this.type = type;
    }
}