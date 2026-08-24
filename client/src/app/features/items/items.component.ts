import { Component, OnInit, inject, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ItemDto, ItemCategory } from '../../core/models/item.model';
import { ItemService } from '../../core/services/item.service';
import { CampaignState } from '../../core/state/campaign.state';


@Component({
  selector: 'app-items',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './items.component.html',
  styleUrls: ['./items.component.css']
})
export class ItemsComponent implements OnInit {
  items: ItemDto[] = [];
  showForm = false;
  editingId: number | null = null;
  newItem: Partial<ItemDto> = { quantity: 1, isIdentified: true };

  itemCategories = Object.values(ItemCategory);

  private itemService = inject(ItemService);
  private campaignState = inject(CampaignState);
  activeCampaign = this.campaignState.activeCampaign;

  constructor() {
    effect(() => {
      const campaign = this.activeCampaign();
      if (campaign && campaign.id) {
        this.loadItems(campaign.id);
      } else {
        this.items = [];
      }
    });
  }

  loadItems(campaignId: number) {
    this.itemService.getItemsByCampaign(campaignId).subscribe(items => {
      this.items = items;
    });
  }

  ngOnInit(): void {
  }

  openForm(item?: ItemDto) {
    this.showForm = true;
    if (item) {
      this.editingId = item.id || null;
      this.newItem = { ...item };
    } else {
      this.editingId = null;
      this.newItem = { quantity: 1, isIdentified: true };
    }
  }

  cancelForm() {
    this.showForm = false;
    this.editingId = null;
    this.newItem = { quantity: 1, isIdentified: true };
  }

  saveItem() {
    if (this.newItem.name) {
      if (this.editingId) {
        const index = this.items.findIndex(i => i.id === this.editingId);
        if (index !== -1) {
          this.itemService.updateItem(this.editingId, { ...this.items[index], ...this.newItem } as ItemDto).subscribe(updated => {
            this.items[index] = updated;
          });
        }
      } else {
        const item: ItemDto = {
          id: Date.now(),
          campaignId: this.activeCampaign()?.id || 0,
          name: this.newItem.name,
          description: this.newItem.description || '',
          category: this.newItem.category,
          quantity: this.newItem.quantity || 1,
          weight: this.newItem.weight || 0,
          value: this.newItem.value || 0,
          isEquipped: this.newItem.isEquipped || false,
          isIdentified: this.newItem.isIdentified !== false // default true
        };
        this.itemService.createItem(item).subscribe(created => {
          this.items.push(created);
        });
      }
      this.cancelForm();
    }
  }

  deleteItem(id: number | undefined) {
    if (id && confirm('Are you sure you want to delete this item?')) {
      this.itemService.deleteItem(id).subscribe(() => {
        this.items = this.items.filter(i => i.id !== id);
      });
    }
  }
}
