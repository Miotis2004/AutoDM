export enum CampaignEventType {
  SESSION_START = 'SESSION_START',
  SESSION_END = 'SESSION_END',
  COMBAT_START = 'COMBAT_START',
  COMBAT_END = 'COMBAT_END',
  QUEST_UPDATE = 'QUEST_UPDATE',
  CHARACTER_LEVEL_UP = 'CHARACTER_LEVEL_UP',
  CHARACTER_DEATH = 'CHARACTER_DEATH',
  LOCATION_DISCOVERED = 'LOCATION_DISCOVERED',
  NARRATIVE = 'NARRATIVE',
  SYSTEM = 'SYSTEM',
}

export interface CampaignEventDto {
  id?: number;
  campaignId: number;
  sessionId?: number;
  type: CampaignEventType;
  description: string;
  timestamp?: string;
  relatedEntityId?: number;
  relatedEntityType?: string;
}

export interface GameSessionDto {
  id?: number;
  campaignId: number;
  sessionNumber?: number;
  startTime?: string;
  endTime?: string;
  summary?: string;
}
