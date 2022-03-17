import { BetItemStatus } from "./enum/bet-item-status";
import { Position } from "./position";

export interface BetItem {
    id: number,
    points: number,
    positions: Position[],
    type: string,
    status: BetItemStatus
}