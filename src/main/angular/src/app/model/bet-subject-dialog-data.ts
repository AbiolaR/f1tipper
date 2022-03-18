import { BetSubjectType } from "./enum/bet-subject-type";

export interface BetSubjectDialogData {
    type: BetSubjectType,
    raceId: number,
    excludeBetSubjects: number[]
}