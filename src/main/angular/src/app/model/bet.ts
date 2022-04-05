import { Race } from "./race";

export interface Bet {
    id: number;
    /*title: string;
    name: string;
    status: string;
    trackSvg: string;
    flagImageUrl: string;*/
    type: string;
    points: number;
    status: string;
    /*dateRange: string;
    raceId: number;
    country: number;*/
    race: Race
}