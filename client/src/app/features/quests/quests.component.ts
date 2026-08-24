import { Component, OnInit, inject, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { QuestDto, QuestStatus } from '../../core/models/quest.model';
import { QuestService } from '../../core/services/quest.service';
import { CampaignState } from '../../core/state/campaign.state';


@Component({
  selector: 'app-quests',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './quests.component.html',
  styleUrls: ['./quests.component.css']
})
export class QuestsComponent implements OnInit {
  quests: QuestDto[] = [];
  showForm = false;
  editingId: number | null = null;
  newQuest: Partial<QuestDto> = { status: QuestStatus.NOT_STARTED };

  questStatuses = Object.values(QuestStatus);

    private questService = inject(QuestService);
  private campaignState = inject(CampaignState);
  activeCampaign = this.campaignState.activeCampaign;

  constructor() {
    effect(() => {
      const campaign = this.activeCampaign();
      if (campaign && campaign.id) {
        this.loadQuests(campaign.id);
      } else {
        this.quests = [];
      }
    });
  }

  loadQuests(campaignId: number) {
    this.questService.getQuestsByCampaign(campaignId).subscribe(quests => {
      this.quests = quests;
    });
  }

  ngOnInit(): void {
  }

  openForm(quest?: QuestDto) {
    this.showForm = true;
    if (quest) {
      this.editingId = quest.id || null;
      this.newQuest = { ...quest };
    } else {
      this.editingId = null;
      this.newQuest = { status: QuestStatus.NOT_STARTED };
    }
  }

  cancelForm() {
    this.showForm = false;
    this.editingId = null;
    this.newQuest = { status: QuestStatus.NOT_STARTED };
  }

  saveQuest() {
    if (this.newQuest.title) {
      if (this.editingId) {
        const index = this.quests.findIndex(q => q.id === this.editingId);
        if (index !== -1) {
          this.questService.updateQuest(this.editingId, { ...this.quests[index], ...this.newQuest } as QuestDto).subscribe(updated => {
            this.quests[index] = updated;
          });
        }
      } else {
        const quest: QuestDto = {
          id: Date.now(),
          campaignId: this.activeCampaign()?.id || 0,
          title: this.newQuest.title,
          description: this.newQuest.description || '',
          status: this.newQuest.status || QuestStatus.NOT_STARTED,
          rewards: this.newQuest.rewards || '',
          objectives: []
        };
        this.questService.createQuest(quest.campaignId, quest).subscribe(created => {
          this.quests.push(created);
        });
      }
      this.cancelForm();
    }
  }

  deleteQuest(id: number | undefined) {
    if (id && confirm('Are you sure you want to delete this quest?')) {
      this.questService.deleteQuest(id).subscribe(() => {
        this.quests = this.quests.filter(q => q.id !== id);
      });
    }
  }
}
