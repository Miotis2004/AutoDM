import { Component, OnInit, inject, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CampaignState } from '../../core/state';
import { SessionService } from '../../core/services';
import { GameSessionDto } from '../../core/models';

@Component({
  selector: 'app-sessions',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sessions.component.html',
  styleUrls: ['./sessions.component.css']
})
export class SessionsComponent implements OnInit {
  private campaignState = inject(CampaignState);
  private sessionService = inject(SessionService);

  hasActiveCampaign = this.campaignState.hasActiveCampaign;
  activeCampaign = this.campaignState.activeCampaign;

  sessions = signal<GameSessionDto[]>([]);
  isLoading = signal<boolean>(false);
  activeSessionId = signal<number | null>(null);

  constructor() {
    effect(() => {
      const campaign = this.activeCampaign();
      if (campaign && campaign.id) {
        this.loadSessions(campaign.id);
      } else {
        this.sessions.set([]);
        this.activeSessionId.set(null);
      }
    }, { allowSignalWrites: true });
  }

  ngOnInit(): void {
  }

  loadSessions(campaignId: number): void {
    this.isLoading.set(true);
    this.sessionService.getCampaignSessions(campaignId).subscribe({
      next: (sessions) => {
        this.sessions.set(sessions);
        const activeSession = sessions.find(s => !s.endTime);
        this.activeSessionId.set(activeSession?.id || null);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error loading sessions', err);
        this.isLoading.set(false);
      }
    });
  }

  startSession(): void {
    const campaignId = this.activeCampaign()?.id;
    if (!campaignId) return;

    this.isLoading.set(true);
    this.sessionService.startSession(campaignId).subscribe({
      next: () => {
        this.loadSessions(campaignId);
      },
      error: (err) => {
        console.error('Error starting session', err);
        this.isLoading.set(false);
      }
    });
  }

  endSession(sessionId: number): void {
    const campaignId = this.activeCampaign()?.id;
    if (!campaignId) return;

    this.isLoading.set(true);
    this.sessionService.endSession(sessionId).subscribe({
      next: () => {
        this.loadSessions(campaignId);
      },
      error: (err) => {
        console.error('Error ending session', err);
        this.isLoading.set(false);
      }
    });
  }
}
