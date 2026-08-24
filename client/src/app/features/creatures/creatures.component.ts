import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CreatureTemplateDto } from '../../core/models/creature.model';

@Component({
  selector: 'app-creatures',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './creatures.component.html',
  styleUrls: ['./creatures.component.css']
})
export class CreaturesComponent implements OnInit {
  creatures: CreatureTemplateDto[] = [];
  showForm = false;
  editingId: number | null = null;
  newCreature: Partial<CreatureTemplateDto> = { challengeRating: 1, hitPoints: 10, armorClass: 10 };

  constructor() {}

  ngOnInit(): void {
    // In a real implementation, load these from a service
  }

  openForm(creature?: CreatureTemplateDto) {
    this.showForm = true;
    if (creature) {
      this.editingId = creature.id || null;
      this.newCreature = { ...creature };
    } else {
      this.editingId = null;
      this.newCreature = { challengeRating: 1, hitPoints: 10, armorClass: 10 };
    }
  }

  cancelForm() {
    this.showForm = false;
    this.editingId = null;
    this.newCreature = { challengeRating: 1, hitPoints: 10, armorClass: 10 };
  }

  saveCreature() {
    if (this.newCreature.name) {
      if (this.editingId) {
        const index = this.creatures.findIndex(c => c.id === this.editingId);
        if (index !== -1) {
          this.creatures[index] = { ...this.creatures[index], ...this.newCreature } as CreatureTemplateDto;
        }
      } else {
        const creature: CreatureTemplateDto = {
          id: Date.now(),
          name: this.newCreature.name,
          description: this.newCreature.description || '',
          size: this.newCreature.size || '',
          type: this.newCreature.type || '',
          alignment: this.newCreature.alignment || '',
          armorClass: this.newCreature.armorClass || 10,
          hitPoints: this.newCreature.hitPoints || 10,
          challengeRating: this.newCreature.challengeRating || 1,
        };
        this.creatures.push(creature);
      }
      this.cancelForm();
    }
  }

  deleteCreature(id: number | undefined) {
    if (id && confirm('Are you sure you want to delete this creature?')) {
      this.creatures = this.creatures.filter(c => c.id !== id);
    }
  }
}
