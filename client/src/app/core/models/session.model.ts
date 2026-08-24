export enum CampaignEventType {
  SESSION_START = 'SESSION_START',
  LOCATION_ENTRY = 'LOCATION_ENTRY',
  DISCOVERY = 'DISCOVERY',
  COMBAT = 'COMBAT',
  DAMAGE = 'DAMAGE',
  ITEM_ACQUISITION = 'ITEM_ACQUISITION',
  QUEST_CHANGE = 'QUEST_CHANGE',
  RELATIONSHIP_CHANGE = 'RELATIONSHIP_CHANGE',
  SESSION_END = 'SESSION_END',
  SHORT_REST = 'SHORT_REST',
  LONG_REST = 'LONG_REST',
}

export interface CampaignEventDto {
  id?: number;
  campaignId: number;
  sessionId?: number;
  eventType: CampaignEventType;
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
