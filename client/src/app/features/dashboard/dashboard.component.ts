import { Component, OnInit, inject, signal, effect, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CampaignState } from '../../core/state';
import {
  CampaignService,
  CharacterService,
  QuestService,
  WorldService,
  DungeonMasterService,
  SessionService
} from '../../core/services';
import {
  CampaignDto,
  PlayerCharacterDto,
  QuestDto,
  LocationDto,
  SceneInfo,
  CampaignEventDto,
  QuestStatus
} from '../../core/models';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  private campaignState = inject(CampaignState);
  private campaignService = inject(CampaignService);
  private characterService = inject(CharacterService);
  private questService = inject(QuestService);
  private worldService = inject(WorldService);
  private dmService = inject(DungeonMasterService);
  private sessionService = inject(SessionService);

  activeCampaign = this.campaignState.activeCampaign;
  hasActiveCampaign = this.campaignState.hasActiveCampaign;

  currentLocation = signal<LocationDto | null>(null);
  activeCharacters = signal<PlayerCharacterDto[]>([]);
  activeQuests = signal<QuestDto[]>([]);
  currentScene = signal<SceneInfo | null>(null);
  recentEvents = signal<CampaignEventDto[]>([]);

  isLoading = signal<boolean>(false);

  constructor() {
    effect(() => {
      const campaign = this.activeCampaign();
      if (campaign && campaign.id) {
        this.loadDashboardData(campaign.id, campaign);
      } else {
        this.resetDashboardData();
      }
    });
  }

  ngOnInit() {
    // If no campaign is active but there are campaigns, maybe set one (for dev/simplicity)
    // We'll just fetch all campaigns and set the first active one if no active campaign is set.
    if (!this.hasActiveCampaign()) {
      this.campaignService.getAllCampaigns().subscribe(campaigns => {
        if (campaigns && campaigns.length > 0) {
          const active = campaigns.find(c => c.status === 'ACTIVE') || campaigns[0];
          this.campaignState.setActiveCampaign(active);
        }
      });
    }
  }

  private loadDashboardData(campaignId: number, campaign: CampaignDto) {
    this.isLoading.set(true);

    const locationReq = campaign.currentLocationId
      ? this.worldService.getLocation(campaign.currentLocationId).pipe(catchError(() => of(null)))
      : of(null);
    const charsReq = this.characterService.getCharactersByCampaign(campaignId).pipe(catchError(() => of([])));
    const questsReq = this.questService.getQuestsByCampaign(campaignId).pipe(catchError(() => of([])));
    const sceneReq = this.dmService.getCurrentScene(campaignId).pipe(catchError(() => of(null)));
    const eventsReq = this.sessionService.getCampaignEvents(campaignId).pipe(catchError(() => of([])));

    forkJoin({
      location: locationReq,
      characters: charsReq,
      quests: questsReq,
      scene: sceneReq,
      events: eventsReq
    }).subscribe({
      next: (data) => {
        this.currentLocation.set(data.location);
        this.activeCharacters.set(data.characters.filter(c => !c.isDead));
        this.activeQuests.set(data.quests.filter(q => q.status === QuestStatus.IN_PROGRESS));
        this.currentScene.set(data.scene);
        this.recentEvents.set(data.events.slice(0, 5)); // Last 5 events
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error loading dashboard data', err);
        this.isLoading.set(false);
      }
    });
  }

  private resetDashboardData() {
    this.currentLocation.set(null);
    this.activeCharacters.set([]);
    this.activeQuests.set([]);
    this.currentScene.set(null);
    this.recentEvents.set([]);
  }
}
