import { Component, OnInit, inject, signal, computed, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CampaignState } from '../../core/state';
import { SessionService } from '../../core/services';
import { CampaignEventDto, CampaignEventType } from '../../core/models';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
  private campaignState = inject(CampaignState);
  private sessionService = inject(SessionService);

  hasActiveCampaign = this.campaignState.hasActiveCampaign;
  activeCampaign = this.campaignState.activeCampaign;

  allEvents = signal<CampaignEventDto[]>([]);
  isLoading = signal<boolean>(false);

  eventTypes = Object.values(CampaignEventType);
  selectedFilter = signal<string>('ALL');

  filteredEvents = computed(() => {
    const filter = this.selectedFilter();
    const events = this.allEvents();
    if (filter === 'ALL') {
      return events;
    }
    return events.filter(e => e.eventType === filter);
  });

  constructor() {
    effect(() => {
      const campaign = this.activeCampaign();
      if (campaign && campaign.id) {
        this.loadEvents(campaign.id);
      } else {
        this.allEvents.set([]);
      }
    }, { allowSignalWrites: true });
  }

  ngOnInit(): void {
  }

  loadEvents(campaignId: number): void {
    this.isLoading.set(true);
    this.sessionService.getCampaignEvents(campaignId).subscribe({
      next: (events) => {
        // Sort descending by timestamp (newest first)
        const sortedEvents = events.sort((a, b) => {
          const dateA = a.timestamp ? new Date(a.timestamp).getTime() : 0;
          const dateB = b.timestamp ? new Date(b.timestamp).getTime() : 0;
          return dateB - dateA;
        });
        this.allEvents.set(sortedEvents);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error loading events', err);
        this.isLoading.set(false);
      }
    });
  }
}
