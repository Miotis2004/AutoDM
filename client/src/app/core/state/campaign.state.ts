import { Injectable, signal, computed } from '@angular/core';
import { CampaignDto } from '../models';

@Injectable({
  providedIn: 'root'
})
export class CampaignState {
  private activeCampaignSignal = signal<CampaignDto | null>(null);

  activeCampaign = this.activeCampaignSignal.asReadonly();
  hasActiveCampaign = computed(() => this.activeCampaignSignal() !== null);

  setActiveCampaign(campaign: CampaignDto | null) {
    this.activeCampaignSignal.set(campaign);
  }
}
