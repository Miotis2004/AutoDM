import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CampaignState } from '../../core/state';
import {
  DungeonMasterService,
  SessionService,
  CharacterService,
  WorldService,
  NpcService
} from '../../core/services';
import {
  SceneInfo,
  PlayerAction,
  ActionResponse,
  PlayerCharacterDto,
  LocationDto,
  NpcDto,
  CampaignEventDto,
  PlayerActionType,
  SceneStatus
} from '../../core/models';

export interface LogEntry {
  id: number;
  type: 'DM' | 'PLAYER' | 'MECHANICS' | 'SYSTEM' | 'EVENT';
  text: string;
  characterName?: string;
  timestamp: Date;
}

@Component({
  selector: 'app-play',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.css']
})
export class PlayComponent implements OnInit {
  campaignState = inject(CampaignState);
  dmService = inject(DungeonMasterService);
  sessionService = inject(SessionService);
  characterService = inject(CharacterService);
  worldService = inject(WorldService);
  npcService = inject(NpcService);

  // State signals
  scene = signal<SceneInfo | null>(null);
  characters = signal<PlayerCharacterDto[]>([]);
  currentLocation = signal<LocationDto | null>(null);
  activeNpcs = signal<NpcDto[]>([]);
  recentEvents = signal<CampaignEventDto[]>([]);
  gameLog = signal<LogEntry[]>([]);

  // Form signals
  actionTypes = Object.values(PlayerActionType);
  selectedCharacterId = signal<number | null>(null);
  selectedActionType = signal<PlayerActionType>(PlayerActionType.OTHER);
  actionDescription = signal<string>('');

  // Loading and Error state
  isLoading = signal<boolean>(false);
  errorMessage = signal<string | null>(null);

  private logIdCounter = 0;

  ngOnInit(): void {
    if (this.campaignState.hasActiveCampaign()) {
      this.loadGameState();
    }
  }

  loadGameState(): void {
    const campaignId = this.campaignState.activeCampaign()?.id;
    if (!campaignId) return;

    this.isLoading.set(true);
    this.errorMessage.set(null);

    // Fetch basic entities
    this.characterService.getCharactersByCampaign(campaignId).subscribe({
      next: (chars) => {
        this.characters.set(chars);
        if (chars.length > 0) {
          this.selectedCharacterId.set(chars[0].id!);
        }
      },
      error: (err) => this.handleError('Failed to load characters', err)
    });

    this.sessionService.getCampaignEvents(campaignId).subscribe({
      next: (events) => {
        this.recentEvents.set(events);
        this.populateLogFromEvents(events);
      },
      error: (err) => this.handleError('Failed to load events', err)
    });

    // Fetch current scene which drives location and NPCs
    this.dmService.getCurrentScene(campaignId).subscribe({
      next: (sceneInfo) => {
        this.scene.set(sceneInfo);
        this.addLogEntry('DM', sceneInfo.narrativeText);

        if (sceneInfo.locationId) {
          this.worldService.getLocation(sceneInfo.locationId).subscribe({
            next: (loc) => this.currentLocation.set(loc),
            error: (err) => this.handleError('Failed to load location', err)
          });
        }

        if (sceneInfo.activeNpcIds && sceneInfo.activeNpcIds.length > 0) {
          // Simplification: Normally would fetch in parallel or bulk
          const npcs: NpcDto[] = [];
          sceneInfo.activeNpcIds.forEach(id => {
            this.npcService.getNpc(id).subscribe({
              next: (npc) => {
                npcs.push(npc);
                this.activeNpcs.set([...npcs]);
              },
              error: (err) => this.handleError(`Failed to load NPC ${id}`, err)
            });
          });
        }

        this.isLoading.set(false);
      },
      error: (err) => {
        this.handleError('Failed to load current scene', err);
        this.isLoading.set(false);
      }
    });
  }

  submitAction(): void {
    const campaignId = this.campaignState.activeCampaign()?.id;
    const charId = this.selectedCharacterId();

    if (!campaignId || !charId || !this.actionDescription().trim()) {
      return;
    }

    const action: PlayerAction = {
      characterId: charId,
      actionType: this.selectedActionType(),
      description: this.actionDescription()
    };

    const character = this.characters().find(c => c.id === charId);

    this.addLogEntry('PLAYER', `[${this.selectedActionType()}] ${this.actionDescription()}`, character?.name);

    this.isLoading.set(true);
    this.dmService.handleAction(campaignId, action).subscribe({
      next: (response) => {
        if (response.mechanicsResult) {
          this.addLogEntry('MECHANICS', response.mechanicsResult);
        }
        if (response.stateChanges && response.stateChanges.length > 0) {
          response.stateChanges.forEach(change => {
            this.addLogEntry('SYSTEM', change);
          });
        }
        this.addLogEntry('DM', response.narrativeResult);

        // Refresh scene after action
        this.refreshScene(campaignId);

        this.actionDescription.set('');
        this.isLoading.set(false);
      },
      error: (err) => {
        this.handleError('Action failed', err);
        this.isLoading.set(false);
      }
    });
  }

  private refreshScene(campaignId: number): void {
     this.dmService.getCurrentScene(campaignId).subscribe({
       next: (sceneInfo) => {
         this.scene.set(sceneInfo);
       }
     });
  }

  private addLogEntry(type: LogEntry['type'], text: string, characterName?: string): void {
    const newEntry: LogEntry = {
      id: this.logIdCounter++,
      type,
      text,
      characterName,
      timestamp: new Date()
    };
    this.gameLog.update(logs => [...logs, newEntry]);

    // In a real app, we would scroll to bottom here
    setTimeout(() => {
        const logContainer = document.querySelector('.narrative-log-container');
        if (logContainer) {
            logContainer.scrollTop = logContainer.scrollHeight;
        }
    }, 100);
  }

  private populateLogFromEvents(events: CampaignEventDto[]): void {
    // Only taking a few recent events for context
    const recent = [...events].sort((a, b) => {
        const dateA = a.timestamp ? new Date(a.timestamp).getTime() : 0;
        const dateB = b.timestamp ? new Date(b.timestamp).getTime() : 0;
        return dateB - dateA;
    }).slice(0, 5).reverse();

    recent.forEach(e => {
        this.addLogEntry('EVENT', `[${e.eventType}] ${e.description}`);
    });
  }

  private handleError(message: string, error: any): void {
    console.error(message, error);
    this.errorMessage.set(`${message}: ${error.message || 'Unknown error'}`);
  }
}
