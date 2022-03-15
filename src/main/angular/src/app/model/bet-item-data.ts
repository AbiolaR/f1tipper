import { BetDataType } from "./enum/bet-data-type";


export class BetItemData {
    id: number | undefined;
    type: BetDataType;

    constructor(id: number | undefined, type: BetDataType) {
        this.id = id;
        this.type = type;
    }
}