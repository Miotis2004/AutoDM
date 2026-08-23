export enum SceneStatus {
  NARRATIVE = 'NARRATIVE',
  COMBAT = 'COMBAT',
  DIALOGUE = 'DIALOGUE',
  EXPLORATION = 'EXPLORATION',
}

export interface SceneInfo {
  campaignId: number;
  locationId?: number;
  status: SceneStatus;
  narrativeText: string;
  activeNpcIds?: number[];
  encounterId?: number;
}

export enum PlayerActionType {
  ATTACK = 'ATTACK',
  CAST_SPELL = 'CAST_SPELL',
  USE_SKILL = 'USE_SKILL',
  MOVE = 'MOVE',
  INTERACT = 'INTERACT',
  DIALOGUE = 'DIALOGUE',
  OTHER = 'OTHER',
}

export interface PlayerAction {
  characterId: number;
  actionType: PlayerActionType;
  description: string;
  targetId?: number;
}

export interface ActionResponse {
  success: boolean;
  narrativeResult: string;
  mechanicsResult?: string;
  stateChanges?: string[];
}
