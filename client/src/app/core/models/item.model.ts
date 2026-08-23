export enum ItemCategory {
  WEAPON = 'WEAPON',
  ARMOR = 'ARMOR',
  POTION = 'POTION',
  SCROLL = 'SCROLL',
  MAGIC_ITEM = 'MAGIC_ITEM',
  GEAR = 'GEAR',
  VALUABLE = 'VALUABLE',
  QUEST_ITEM = 'QUEST_ITEM',
  OTHER = 'OTHER',
}

export interface ItemDto {
  id?: number;
  campaignId: number;
  characterId?: number;
  name: string;
  description?: string;
  category?: ItemCategory;
  quantity: number;
  weight?: number;
  value?: number;
  isEquipped?: boolean;
  isIdentified?: boolean;
}
