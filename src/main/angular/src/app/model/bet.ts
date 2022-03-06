import { Driver } from "./driver";
import { Position } from "./position";

export interface Bet {
    id: number,
    points: number,
    positions: Position[],
    type: string
}