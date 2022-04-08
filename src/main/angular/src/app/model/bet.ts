import { Race } from "./race";

export interface Bet {
    id: number;
    type: string;
    points: number;
    status: string;
    race: Race
}