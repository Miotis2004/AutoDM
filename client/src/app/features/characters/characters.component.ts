import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PlayerCharacterDto } from '../../core/models/character.model';

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

  constructor() {}

  ngOnInit(): void {
    // In a real implementation, load these from a service
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
          this.characters[index] = { ...this.characters[index], ...this.newCharacter } as PlayerCharacterDto;
        }
      } else {
        const character: PlayerCharacterDto = {
          id: Date.now(),
          campaignId: 1, // hardcoded for now
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
        this.characters.push(character);
      }
      this.cancelForm();
    }
  }

  deleteCharacter(id: number | undefined) {
    if (id && confirm('Are you sure you want to delete this character?')) {
      this.characters = this.characters.filter(c => c.id !== id);
    }
  }
}
