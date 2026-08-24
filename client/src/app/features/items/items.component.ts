import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ItemDto, ItemCategory } from '../../core/models/item.model';

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

  constructor() {}

  ngOnInit(): void {
    // In a real implementation, load these from a service
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
          this.items[index] = { ...this.items[index], ...this.newItem } as ItemDto;
        }
      } else {
        const item: ItemDto = {
          id: Date.now(),
          campaignId: 1, // hardcoded for now
          name: this.newItem.name,
          description: this.newItem.description || '',
          category: this.newItem.category,
          quantity: this.newItem.quantity || 1,
          weight: this.newItem.weight || 0,
          value: this.newItem.value || 0,
          isEquipped: this.newItem.isEquipped || false,
          isIdentified: this.newItem.isIdentified !== false // default true
        };
        this.items.push(item);
      }
      this.cancelForm();
    }
  }

  deleteItem(id: number | undefined) {
    if (id && confirm('Are you sure you want to delete this item?')) {
      this.items = this.items.filter(i => i.id !== id);
    }
  }
}
