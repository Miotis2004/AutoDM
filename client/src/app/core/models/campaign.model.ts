export enum CampaignStatus {
  ACTIVE = 'ACTIVE',
  PAUSED = 'PAUSED',
  COMPLETED = 'COMPLETED',
  ARCHIVED = 'ARCHIVED',
}

export interface CampaignDto {
  id?: number;
  title: string;
  description?: string;
  status: CampaignStatus;
  creationDate?: string;
  lastPlayedDate?: string;
  notes?: string;
  currentLocationId?: number;
  currentSceneId?: number;
}
