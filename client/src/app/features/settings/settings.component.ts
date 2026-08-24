import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SettingsService } from '../../core/services';
import { UserSettings } from '../../core/models';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="settings-container p-4 max-w-4xl mx-auto">
      <h1 class="text-3xl font-bold mb-6">Settings</h1>

      <!-- System Settings -->
      <section class="mb-8 border rounded-lg p-6 bg-white dark:bg-gray-800 shadow">
        <h2 class="text-xl font-semibold mb-4 border-b pb-2">System Configuration</h2>

        @if (systemSettings()) {
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">Data Directory</label>
              <div class="mt-1 p-2 bg-gray-100 dark:bg-gray-700 rounded font-mono text-sm break-all">
                {{ systemSettings()?.dataDir }}
              </div>
              <p class="mt-1 text-xs text-gray-500">The local path where application data is stored.</p>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">Database Name</label>
              <div class="mt-1 p-2 bg-gray-100 dark:bg-gray-700 rounded font-mono text-sm break-all">
                {{ systemSettings()?.dbName }}
              </div>
              <p class="mt-1 text-xs text-gray-500">The name of the local SQLite database file.</p>
            </div>
          </div>
        } @else {
          <div class="empty-state py-4 text-center text-gray-500">
            <p>Loading system settings...</p>
          </div>
        }
      </section>

      <!-- Display Preferences -->
      <section class="border rounded-lg p-6 bg-white dark:bg-gray-800 shadow">
        <h2 class="text-xl font-semibold mb-4 border-b pb-2">Display Preferences</h2>

        <form class="space-y-6">
          <!-- Theme -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Theme</label>
            <div class="flex space-x-4">
              <label class="inline-flex items-center">
                <input type="radio"
                       name="theme"
                       value="light"
                       [ngModel]="userSettings().theme"
                       (ngModelChange)="onThemeChange($event)"
                       class="form-radio text-indigo-600">
                <span class="ml-2">Light</span>
              </label>
              <label class="inline-flex items-center">
                <input type="radio"
                       name="theme"
                       value="dark"
                       [ngModel]="userSettings().theme"
                       (ngModelChange)="onThemeChange($event)"
                       class="form-radio text-indigo-600">
                <span class="ml-2">Dark</span>
              </label>
            </div>
          </div>

          <!-- Font Size -->
          <div>
            <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Font Size</label>
            <select
              name="fontSize"
              [ngModel]="userSettings().fontSize"
              (ngModelChange)="onFontSizeChange($event)"
              class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md dark:bg-gray-700 dark:border-gray-600">
              <option value="small">Small</option>
              <option value="medium">Medium (Default)</option>
              <option value="large">Large</option>
            </select>
          </div>
        </form>
      </section>
    </div>
  `
})
export class SettingsComponent implements OnInit {
  private settingsService = inject(SettingsService);

  systemSettings = this.settingsService.systemSettings;
  userSettings = this.settingsService.userSettings;

  ngOnInit() {
    this.settingsService.fetchSystemSettings().subscribe({
      error: (err) => console.error('Failed to load system settings', err)
    });
  }

  onThemeChange(theme: 'light' | 'dark') {
    this.settingsService.updateUserSettings({ theme });
  }

  onFontSizeChange(fontSize: 'small' | 'medium' | 'large') {
    this.settingsService.updateUserSettings({ fontSize });
  }
}
