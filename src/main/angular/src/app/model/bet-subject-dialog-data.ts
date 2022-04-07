import { BetSubject } from "./bet-subject";
import { BetSubjectType } from "./enum/bet-subject-type";

export interface BetSubjectDialogData {
    type: BetSubjectType,
    raceId: number,
    betSubjects: BetSubject[],
    excludeBetSubjects: number[]
}