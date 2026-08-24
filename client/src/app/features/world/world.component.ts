import { Component, OnInit, inject, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LocationDto, LocationType } from '../../core/models/location.model';
import { WorldService } from '../../core/services/world.service';
import { CampaignState } from '../../core/state/campaign.state';

import { FactionDto } from '../../core/models/faction.model';

@Component({
  selector: 'app-world',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './world.component.html',
  styleUrls: ['./world.component.css']
})
export class WorldComponent implements OnInit {
  locations: LocationDto[] = [];
  factions: FactionDto[] = [];

  showLocationForm = false;
  showFactionForm = false;

  editingLocationId: number | null = null;
  editingFactionId: number | null = null;

  newLocation: Partial<LocationDto> = {};
  newFaction: Partial<FactionDto> = {};

  selectedTab: 'locations' | 'factions' = 'locations';

  locationTypes = Object.values(LocationType);

    private worldService = inject(WorldService);
  private campaignState = inject(CampaignState);
  activeCampaign = this.campaignState.activeCampaign;

  constructor() {
    effect(() => {
      const campaign = this.activeCampaign();
      if (campaign && campaign.id) {
        this.loadWorldData(campaign.id);
      } else {
        this.locations = [];
        this.factions = [];
      }
    });
  }

  loadWorldData(campaignId: number) {
    this.worldService.getLocationsByCampaign(campaignId).subscribe(locs => {
      this.locations = locs;
    });
    this.worldService.getFactionsByCampaign(campaignId).subscribe(facs => {
      this.factions = facs;
    });
  }

  ngOnInit(): void {
  }

  setTab(tab: 'locations' | 'factions') {
    this.selectedTab = tab;
  }

  openLocationForm(location?: LocationDto) {
    this.showLocationForm = true;
    if (location) {
      this.editingLocationId = location.id || null;
      this.newLocation = { ...location };
    } else {
      this.editingLocationId = null;
      this.newLocation = {};
    }
  }

  cancelLocationForm() {
    this.showLocationForm = false;
    this.editingLocationId = null;
    this.newLocation = {};
  }

  saveLocation() {
    if (this.newLocation.name) {
      if (this.editingLocationId) {
        const index = this.locations.findIndex(l => l.id === this.editingLocationId);
        if (index !== -1) {
          this.worldService.updateLocation(this.editingLocationId, { ...this.locations[index], ...this.newLocation } as LocationDto).subscribe(updated => {
            this.locations[index] = updated;
          });
        }
      } else {
        const location: LocationDto = {
          id: Date.now(),
          campaignId: this.activeCampaign()?.id || 0,
          name: this.newLocation.name,
          type: this.newLocation.type,
          description: this.newLocation.description || '',
          isDiscovered: this.newLocation.isDiscovered || false
        };
        this.worldService.createLocation(location.campaignId, location).subscribe(created => {
          this.locations.push(created);
        });
      }
      this.cancelLocationForm();
    }
  }

  openFactionForm(faction?: FactionDto) {
    this.showFactionForm = true;
    if (faction) {
      this.editingFactionId = faction.id || null;
      this.newFaction = { ...faction };
    } else {
      this.editingFactionId = null;
      this.newFaction = {};
    }
  }

  cancelFactionForm() {
    this.showFactionForm = false;
    this.editingFactionId = null;
    this.newFaction = {};
  }

  saveFaction() {
    if (this.newFaction.name) {
      if (this.editingFactionId) {
        const index = this.factions.findIndex(f => f.id === this.editingFactionId);
        if (index !== -1) {
          this.worldService.updateFaction(this.editingFactionId, { ...this.factions[index], ...this.newFaction } as FactionDto).subscribe(updated => {
            this.factions[index] = updated;
          });
        }
      } else {
        const faction: FactionDto = {
          id: Date.now(),
          campaignId: this.activeCampaign()?.id || 0,
          name: this.newFaction.name,
          description: this.newFaction.description || '',
          goals: this.newFaction.goals || ''
        };
        this.worldService.createFaction(faction.campaignId, faction).subscribe(created => {
          this.factions.push(created);
        });
      }
      this.cancelFactionForm();
    }
  }

  deleteLocation(id: number | undefined) {
    if (id && confirm('Are you sure you want to delete this location?')) {
      this.worldService.deleteLocation(id).subscribe(() => {
        this.locations = this.locations.filter(l => l.id !== id);
      });
    }
  }

  deleteFaction(id: number | undefined) {
    if (id && confirm('Are you sure you want to delete this faction?')) {
      this.worldService.deleteFaction(id).subscribe(() => {
        this.factions = this.factions.filter(f => f.id !== id);
      });
    }
  }
}
