import { BetSubjectType } from "./enum/bet-subject-type";

export interface BetSubject{
    id: number,
    type: BetSubjectType,
    name: string,
    imgUrl: string
}

export class EmptyBetSubject implements BetSubject {
    id: number = 0;
    type: BetSubjectType = BetSubjectType.UNKNOWN;
    name: string = "";
    imgUrl: string = "";
}