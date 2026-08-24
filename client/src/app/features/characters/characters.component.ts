import { Component, OnInit, inject, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PlayerCharacterDto } from '../../core/models/character.model';
import { CharacterService } from '../../core/services/character.service';
import { CampaignState } from '../../core/state/campaign.state';


@Component({
  selector: 'app-characters',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './characters.component.html',
  styleUrls: ['./characters.component.css']
})
export class CharactersComponent implements OnInit {
  characters: PlayerCharacterDto[] = [];
  showForm = false;
  editingId: number | null = null;
  newCharacter: Partial<PlayerCharacterDto> = { level: 1, maximumHitPoints: 10, hitPoints: 10, armorClass: 10 };

  private characterService = inject(CharacterService);
  private campaignState = inject(CampaignState);
  activeCampaign = this.campaignState.activeCampaign;

  constructor() {
    effect(() => {
      const campaign = this.activeCampaign();
      if (campaign && campaign.id) {
        this.loadCharacters(campaign.id);
      } else {
        this.characters = [];
      }
    });
  }

  loadCharacters(campaignId: number) {
    this.characterService.getCharactersByCampaign(campaignId).subscribe(chars => {
      this.characters = chars;
    });
  }

  ngOnInit(): void {
  }

  openForm(character?: PlayerCharacterDto) {
    this.showForm = true;
    if (character) {
      this.editingId = character.id || null;
      this.newCharacter = { ...character };
    } else {
      this.editingId = null;
      this.newCharacter = { level: 1, maximumHitPoints: 10, hitPoints: 10, armorClass: 10 };
    }
  }

  cancelForm() {
    this.showForm = false;
    this.editingId = null;
    this.newCharacter = { level: 1, maximumHitPoints: 10, hitPoints: 10, armorClass: 10 };
  }

  saveCharacter() {
    if (this.newCharacter.name) {
      if (this.editingId) {
        const index = this.characters.findIndex(c => c.id === this.editingId);
        if (index !== -1) {
          this.characterService.updateCharacter(this.editingId, { ...this.characters[index], ...this.newCharacter } as PlayerCharacterDto).subscribe(updated => {
            this.characters[index] = updated;
          });
        }
      } else {
        const character: PlayerCharacterDto = {
          id: Date.now(),
          campaignId: this.activeCampaign()?.id || 0,
          name: this.newCharacter.name,
          ancestry: this.newCharacter.ancestry || '',
          characterClass: this.newCharacter.characterClass || '',
          level: this.newCharacter.level || 1,
          maximumHitPoints: this.newCharacter.maximumHitPoints || 10,
          hitPoints: this.newCharacter.hitPoints || 10,
          armorClass: this.newCharacter.armorClass || 10,
          isDead: false,
          isUnconscious: false
        };
        this.characterService.createCharacter(character.campaignId, character).subscribe(created => {
          this.characters.push(created);
        });
      }
      this.cancelForm();
    }
  }

  deleteCharacter(id: number | undefined) {
    if (id && confirm('Are you sure you want to delete this character?')) {
      this.characterService.deleteCharacter(id).subscribe(() => {
        this.characters = this.characters.filter(c => c.id !== id);
      });
    }
  }
}
