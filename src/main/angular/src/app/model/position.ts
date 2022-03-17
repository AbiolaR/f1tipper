import { BetSubject } from "./bet-subject";

export interface Position {
    id: number,
    position: number,
    betSubject: BetSubject,
    fastestLap: boolean
}