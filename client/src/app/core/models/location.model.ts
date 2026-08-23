export enum LocationType {
  REGION = 'REGION',
  CITY = 'CITY',
  TOWN = 'TOWN',
  VILLAGE = 'VILLAGE',
  DUNGEON = 'DUNGEON',
  LANDMARK = 'LANDMARK',
  BUILDING = 'BUILDING',
}

export interface LocationDto {
  id?: number;
  campaignId: number;
  name: string;
  type?: LocationType;
  description?: string;
  isDiscovered?: boolean;
  parentLocationId?: number;
  connectedLocationIds?: number[];
}
