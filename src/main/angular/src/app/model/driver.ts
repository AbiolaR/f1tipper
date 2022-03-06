export interface Driver {
    id: number;
    name: string;
    imgUrl: string;
}

export class EmptyDriver implements Driver {
    id: number = 0;
    name: string = "";
    imgUrl: string = "";
}