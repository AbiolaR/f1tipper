export interface League {
    id: number,
    name: String
}

export const emptyLeague = (): League => ({id: 0, name: ''});