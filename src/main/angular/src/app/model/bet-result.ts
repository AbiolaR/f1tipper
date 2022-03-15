import { BetItem } from "./bet-item";
import { BetDataType } from "./enum/bet-data-type";
import { Position } from "./position";

export interface BetResult {
    betItemType: BetDataType
    betItemResultPositions: BetItemResultPosition[]
}

export interface BetItemResultPosition {
    betItemPosition: Position,
    resultPosition: Position
}