export enum BetDataType {
    RACE = 'race',
    QUALIFYING = 'qualifying',
    DNF = 'dnf'
}

export class BetData {
    id: number | undefined;
    type: BetDataType;

    constructor(id: number | undefined, type: BetDataType) {
        this.id = id;
        this.type = type;
    }
}