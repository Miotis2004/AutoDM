export interface FactionDto {
  id?: number;
  campaignId: number;
  name: string;
  description?: string;
  leaderId?: number;
  baseLocationId?: number;
  allies?: string;
  enemies?: string;
  goals?: string;
}
