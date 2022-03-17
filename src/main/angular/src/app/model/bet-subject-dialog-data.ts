import { BetSubjectType } from "./enum/bet-subject-type";

export interface BetSubjectDialogData {
    type: BetSubjectType,
    excludeBetSubjects: number[]
}