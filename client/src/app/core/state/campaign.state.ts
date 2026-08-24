import { Injectable, signal, computed } from '@angular/core';
import { CampaignDto } from '../models';

@Injectable({
  providedIn: 'root'
})
export class CampaignState {
  private activeCampaignSignal = signal<CampaignDto | null>(null);

  activeCampaign = this.activeCampaignSignal.asReadonly();
  hasActiveCampaign = computed(() => this.activeCampaignSignal() !== null);

  constructor() {
    const stored = localStorage.getItem('activeCampaign');
    if (stored) {
      try {
        this.activeCampaignSignal.set(JSON.parse(stored));
      } catch (e) {
        console.error('Failed to parse active campaign from local storage', e);
      }
    }
  }

  setActiveCampaign(campaign: CampaignDto | null) {
    this.activeCampaignSignal.set(campaign);
    if (campaign) {
      localStorage.setItem('activeCampaign', JSON.stringify(campaign));
    } else {
      localStorage.removeItem('activeCampaign');
    }
  }
}
