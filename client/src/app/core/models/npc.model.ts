export interface NpcDto {
  id?: number;
  campaignId: number;
  locationId?: number;
  factionId?: number;
  name: string;
  race?: string;
  occupation?: string;
  appearance?: string;
  personality?: string;
  secrets?: string;
  isAlive?: boolean;
  attitude?: string;
  notes?: string;
}
