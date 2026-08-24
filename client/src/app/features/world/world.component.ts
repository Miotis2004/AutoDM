import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LocationDto, LocationType } from '../../core/models/location.model';
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

  constructor() {}

  ngOnInit(): void {
    // In a real implementation, load these from a service
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
          this.locations[index] = { ...this.locations[index], ...this.newLocation } as LocationDto;
        }
      } else {
        const location: LocationDto = {
          id: Date.now(),
          campaignId: 1, // hardcoded for now
          name: this.newLocation.name,
          type: this.newLocation.type,
          description: this.newLocation.description || '',
          isDiscovered: this.newLocation.isDiscovered || false
        };
        this.locations.push(location);
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
          this.factions[index] = { ...this.factions[index], ...this.newFaction } as FactionDto;
        }
      } else {
        const faction: FactionDto = {
          id: Date.now(),
          campaignId: 1,
          name: this.newFaction.name,
          description: this.newFaction.description || '',
          goals: this.newFaction.goals || ''
        };
        this.factions.push(faction);
      }
      this.cancelFactionForm();
    }
  }

  deleteLocation(id: number | undefined) {
    if (id && confirm('Are you sure you want to delete this location?')) {
      this.locations = this.locations.filter(l => l.id !== id);
    }
  }

  deleteFaction(id: number | undefined) {
    if (id && confirm('Are you sure you want to delete this faction?')) {
      this.factions = this.factions.filter(f => f.id !== id);
    }
  }
}
