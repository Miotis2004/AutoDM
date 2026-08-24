import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { QuestDto, QuestStatus } from '../../core/models/quest.model';

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

  constructor() {}

  ngOnInit(): void {
    // In a real implementation, load these from a service
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
          this.quests[index] = { ...this.quests[index], ...this.newQuest } as QuestDto;
        }
      } else {
        const quest: QuestDto = {
          id: Date.now(),
          campaignId: 1, // hardcoded for now
          title: this.newQuest.title,
          description: this.newQuest.description || '',
          status: this.newQuest.status || QuestStatus.NOT_STARTED,
          rewards: this.newQuest.rewards || '',
          objectives: []
        };
        this.quests.push(quest);
      }
      this.cancelForm();
    }
  }

  deleteQuest(id: number | undefined) {
    if (id && confirm('Are you sure you want to delete this quest?')) {
      this.quests = this.quests.filter(q => q.id !== id);
    }
  }
}
