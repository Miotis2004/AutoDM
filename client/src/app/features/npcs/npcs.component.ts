import { Component, OnInit, inject, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NpcDto } from '../../core/models/npc.model';
import { NpcService } from '../../core/services/npc.service';
import { CampaignState } from '../../core/state/campaign.state';


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

    private npcService = inject(NpcService);
  private campaignState = inject(CampaignState);
  activeCampaign = this.campaignState.activeCampaign;

  constructor() {
    effect(() => {
      const campaign = this.activeCampaign();
      if (campaign && campaign.id) {
        this.loadNpcs(campaign.id);
      } else {
        this.npcs = [];
      }
    });
  }

  loadNpcs(campaignId: number) {
    this.npcService.getNpcsByCampaign(campaignId).subscribe(npcs => {
      this.npcs = npcs;
    });
  }

  ngOnInit(): void {
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
          this.npcService.updateNpc(this.editingId, { ...this.npcs[index], ...this.newNpc } as NpcDto).subscribe(updated => {
            this.npcs[index] = updated;
          });
        }
      } else {
        const npc: NpcDto = {
          id: Date.now(),
          campaignId: this.activeCampaign()?.id || 0,
          name: this.newNpc.name,
          race: this.newNpc.race || '',
          occupation: this.newNpc.occupation || '',
          appearance: this.newNpc.appearance || '',
          personality: this.newNpc.personality || '',
          isAlive: this.newNpc.isAlive !== false, // default true
          notes: this.newNpc.notes || ''
        };
        this.npcService.createNpc(npc.campaignId, npc).subscribe(created => {
          this.npcs.push(created);
        });
      }
      this.cancelForm();
    }
  }

  deleteNpc(id: number | undefined) {
    if (id && confirm('Are you sure you want to delete this NPC?')) {
      this.npcService.deleteNpc(id).subscribe(() => {
        this.npcs = this.npcs.filter(n => n.id !== id);
      });
    }
  }
}
