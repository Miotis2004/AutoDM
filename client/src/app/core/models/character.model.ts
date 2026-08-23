export interface PlayerCharacterDto {
  id?: number;
  campaignId: number;
  name: string;
  ancestry?: string;
  characterClass?: string;
  level?: number;
  background?: string;
  alignment?: string;
  hitPoints?: number;
  maximumHitPoints?: number;
  temporaryHitPoints?: number;
  isUnconscious?: boolean;
  isDead?: boolean;
  armorClass?: number;
  movement?: number;
  strength?: number;
  dexterity?: number;
  constitution?: number;
  intelligence?: number;
  wisdom?: number;
  charisma?: number;
  savingThrowProficiencies?: string;
  skillProficiencies?: string;
  generalProficiencies?: string;
  conditions?: string;
}

export interface CharacterResourceDto {
  id?: number;
  characterId: number;
  name: string;
  currentValue: number;
  maxValue: number;
  resetCondition?: string;
}
