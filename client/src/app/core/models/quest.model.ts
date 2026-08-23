export enum QuestStatus {
  NOT_STARTED = 'NOT_STARTED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED',
  ABANDONED = 'ABANDONED',
}

export interface ObjectiveDto {
  id?: number;
  questId: number;
  description: string;
  isCompleted?: boolean;
  orderIndex?: number;
}

export interface QuestDto {
  id?: number;
  campaignId: number;
  title: string;
  description?: string;
  status: QuestStatus;
  rewards?: string;
  giverNpcId?: number;
  locationId?: number;
  objectives?: ObjectiveDto[];
}
