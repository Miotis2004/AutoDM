import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CampaignService } from '../../core/services/campaign.service';
import { CampaignState } from '../../core/state/campaign.state';
import { CampaignDto, CampaignStatus } from '../../core/models';

@Component({
  selector: 'app-campaigns',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="campaigns-container">
      <h2>Campaigns Management</h2>

      <!-- Error and Loading States -->
      @if (loading) {
        <p>Loading campaigns...</p>
      }
      @if (error) {
        <p class="error">{{ error }}</p>
      }

      <div class="layout">
        <!-- Campaigns List -->
        <div class="campaign-list">
          <h3>Your Campaigns</h3>
          @if (!loading && campaigns.length === 0) {
            <p>No campaigns found. Create one to get started!</p>
          }

          <ul class="list">
            @for (campaign of campaigns; track campaign.id) {
              <li class="campaign-item" [class.active]="campaign.id === activeCampaignId()">
                <div class="campaign-info">
                  <h4>{{ campaign.title }}</h4>
                  <p>{{ campaign.description }}</p>
                  <p class="status">Status: {{ campaign.status }}</p>
                  @if (campaign.id === activeCampaignId()) {
                    <span class="badge">Active</span>
                  }
                </div>
                <div class="campaign-actions">
                  @if (campaign.id !== activeCampaignId()) {
                    <button (click)="selectActive(campaign)">Select</button>
                  }
                  <button (click)="editCampaign(campaign)">Edit</button>
                  <button (click)="archiveCampaign(campaign)">Archive</button>
                  <button class="danger" (click)="deleteCampaign(campaign)">Delete</button>
                </div>
              </li>
            }
          </ul>
        </div>

        <!-- Campaign Form -->
        <div class="campaign-form">
          <h3>{{ editingId ? 'Edit Campaign' : 'Create Campaign' }}</h3>
          <form [formGroup]="campaignForm" (ngSubmit)="onSubmit()">
            <div class="form-group">
              <label for="title">Title *</label>
              <input id="title" type="text" formControlName="title">
              @if (campaignForm.get('title')?.invalid && campaignForm.get('title')?.touched) {
                <span class="error-text">Title is required.</span>
              }
            </div>

            <div class="form-group">
              <label for="description">Description</label>
              <textarea id="description" formControlName="description"></textarea>
            </div>

            <div class="form-group">
              <label for="notes">Notes</label>
              <textarea id="notes" formControlName="notes"></textarea>
            </div>

            <div class="form-group">
              <label for="status">Status</label>
              <select id="status" formControlName="status">
                <option value="ACTIVE">Active</option>
                <option value="PAUSED">Paused</option>
                <option value="COMPLETED">Completed</option>
                <option value="ARCHIVED">Archived</option>
              </select>
            </div>

            <div class="form-actions">
              <button type="submit" [disabled]="campaignForm.invalid || saving">
                {{ saving ? 'Saving...' : (editingId ? 'Update' : 'Create') }}
              </button>
              @if (editingId) {
                <button type="button" (click)="cancelEdit()">Cancel</button>
              }
            </div>
          </form>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .campaigns-container { padding: 20px; }
    .layout { display: flex; gap: 20px; flex-wrap: wrap; }
    .campaign-list { flex: 1; min-width: 300px; }
    .campaign-form { flex: 1; min-width: 300px; padding: 20px; background: #f5f5f5; border-radius: 8px; }
    .list { list-style: none; padding: 0; }
    .campaign-item { padding: 15px; border: 1px solid #ddd; margin-bottom: 10px; border-radius: 4px; }
    .campaign-item.active { border-color: #007bff; background: #e9f5ff; }
    .campaign-actions { margin-top: 10px; display: flex; gap: 10px; }
    .form-group { margin-bottom: 15px; }
    .form-group label { display: block; margin-bottom: 5px; }
    .form-group input, .form-group textarea, .form-group select { width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; }
    .error { color: red; }
    .error-text { color: red; font-size: 12px; }
    .danger { background-color: #dc3545; color: white; border: none; padding: 5px 10px; border-radius: 4px; cursor: pointer; }
    .badge { background: #007bff; color: white; padding: 2px 5px; border-radius: 3px; font-size: 12px; }
    button { padding: 5px 10px; cursor: pointer; }
  `]
})
export class CampaignsComponent implements OnInit {
  private campaignService = inject(CampaignService);
  private campaignState = inject(CampaignState);
  private fb = inject(FormBuilder);

  campaigns: CampaignDto[] = [];
  loading = false;
  saving = false;
  error: string | null = null;

  campaignForm: FormGroup;
  editingId: number | null = null;

  activeCampaign = this.campaignState.activeCampaign;

  constructor() {
    this.campaignForm = this.fb.group({
      title: ['', Validators.required],
      description: [''],
      notes: [''],
      status: [CampaignStatus.ACTIVE, Validators.required]
    });
  }

  ngOnInit() {
    this.loadCampaigns();
  }

  activeCampaignId(): number | undefined {
    return this.activeCampaign()?.id;
  }

  loadCampaigns() {
    this.loading = true;
    this.error = null;
    this.campaignService.getAllCampaigns().subscribe({
      next: (campaigns) => {
        this.campaigns = campaigns;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load campaigns.';
        console.error(err);
        this.loading = false;
      }
    });
  }

  selectActive(campaign: CampaignDto) {
    this.campaignState.setActiveCampaign(campaign);
  }

  editCampaign(campaign: CampaignDto) {
    this.editingId = campaign.id || null;
    this.campaignForm.patchValue({
      title: campaign.title,
      description: campaign.description || '',
      notes: campaign.notes || '',
      status: campaign.status || CampaignStatus.ACTIVE
    });
  }

  cancelEdit() {
    this.editingId = null;
    this.campaignForm.reset({ status: CampaignStatus.ACTIVE });
  }

  onSubmit() {
    if (this.campaignForm.invalid) {
      this.campaignForm.markAllAsTouched();
      return;
    }

    this.saving = true;
    this.error = null;

    const formData = this.campaignForm.value;
    const campaignDto: CampaignDto = {
      title: formData.title,
      description: formData.description,
      notes: formData.notes,
      status: formData.status
    };

    if (this.editingId) {
      this.campaignService.updateCampaign(this.editingId, campaignDto).subscribe({
        next: (updated) => {
          const index = this.campaigns.findIndex(c => c.id === this.editingId);
          if (index !== -1) {
            this.campaigns[index] = updated;
          }
          if (this.activeCampaignId() === updated.id) {
            this.campaignState.setActiveCampaign(updated);
          }
          this.saving = false;
          this.cancelEdit();
        },
        error: (err) => {
          this.error = 'Failed to update campaign.';
          console.error(err);
          this.saving = false;
        }
      });
    } else {
      this.campaignService.createCampaign(campaignDto).subscribe({
        next: (created) => {
          this.campaigns.push(created);
          this.saving = false;
          this.cancelEdit();
        },
        error: (err) => {
          this.error = 'Failed to create campaign.';
          console.error(err);
          this.saving = false;
        }
      });
    }
  }

  archiveCampaign(campaign: CampaignDto) {
    if (!campaign.id) return;

    if (confirm(`Are you sure you want to archive the campaign "${campaign.title}"?`)) {
      this.campaignService.archiveCampaign(campaign.id).subscribe({
        next: (archived) => {
          const index = this.campaigns.findIndex(c => c.id === archived.id);
          if (index !== -1) {
            this.campaigns[index] = archived;
          }
          if (this.activeCampaignId() === archived.id) {
            this.campaignState.setActiveCampaign(archived);
          }
        },
        error: (err) => {
          this.error = 'Failed to archive campaign.';
          console.error(err);
        }
      });
    }
  }

  deleteCampaign(campaign: CampaignDto) {
    if (!campaign.id) return;

    if (confirm(`Are you sure you want to PERMANENTLY delete the campaign "${campaign.title}"? This action cannot be undone.`)) {
      this.campaignService.deleteCampaign(campaign.id).subscribe({
        next: () => {
          this.campaigns = this.campaigns.filter(c => c.id !== campaign.id);
          if (this.activeCampaignId() === campaign.id) {
            this.campaignState.setActiveCampaign(null);
          }
          if (this.editingId === campaign.id) {
            this.cancelEdit();
          }
        },
        error: (err) => {
          this.error = 'Failed to delete campaign.';
          console.error(err);
        }
      });
    }
  }
}
