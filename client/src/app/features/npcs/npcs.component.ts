import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NpcDto } from '../../core/models/npc.model';

@Component({
  selector: 'app-npcs',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './npcs.component.html',
  styleUrls: ['./npcs.component.css']
})
export class NpcsComponent implements OnInit {
  npcs: NpcDto[] = [];
  showForm = false;
  editingId: number | null = null;
  newNpc: Partial<NpcDto> = { isAlive: true };

  constructor() {}

  ngOnInit(): void {
    // In a real implementation, load these from a service
  }

  openForm(npc?: NpcDto) {
    this.showForm = true;
    if (npc) {
      this.editingId = npc.id || null;
      this.newNpc = { ...npc };
    } else {
      this.editingId = null;
      this.newNpc = { isAlive: true };
    }
  }

  cancelForm() {
    this.showForm = false;
    this.editingId = null;
    this.newNpc = { isAlive: true };
  }

  saveNpc() {
    if (this.newNpc.name) {
      if (this.editingId) {
        const index = this.npcs.findIndex(n => n.id === this.editingId);
        if (index !== -1) {
          this.npcs[index] = { ...this.npcs[index], ...this.newNpc } as NpcDto;
        }
      } else {
        const npc: NpcDto = {
          id: Date.now(),
          campaignId: 1, // hardcoded for now
          name: this.newNpc.name,
          race: this.newNpc.race || '',
          occupation: this.newNpc.occupation || '',
          appearance: this.newNpc.appearance || '',
          personality: this.newNpc.personality || '',
          isAlive: this.newNpc.isAlive !== false, // default true
          notes: this.newNpc.notes || ''
        };
        this.npcs.push(npc);
      }
      this.cancelForm();
    }
  }

  deleteNpc(id: number | undefined) {
    if (id && confirm('Are you sure you want to delete this NPC?')) {
      this.npcs = this.npcs.filter(n => n.id !== id);
    }
  }
}
