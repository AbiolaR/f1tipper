import { Driver } from "./driver";
import { Position } from "./position";

export interface BetItem {
    id: number,
    points: number,
    positions: Position[],
    type: string,
    status: string
}