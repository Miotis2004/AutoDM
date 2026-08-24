import { Injectable, signal, computed, effect } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { SystemSettingsDto, UserSettings } from '../models';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {
  private apiUrl = '/api/settings';

  // Default user settings
  private readonly defaultSettings: UserSettings = {
    theme: 'dark',
    fontSize: 'medium'
  };

  // State signals
  private systemSettingsSignal = signal<SystemSettingsDto | null>(null);
  private userSettingsSignal = signal<UserSettings>(this.loadUserSettings());

  // Public computed values
  systemSettings = computed(() => this.systemSettingsSignal());
  userSettings = computed(() => this.userSettingsSignal());

  constructor(private http: HttpClient) {
    // Save to local storage whenever user settings change
    effect(() => {
      const settings = this.userSettingsSignal();
      localStorage.setItem('autodm_settings', JSON.stringify(settings));
      this.applySettingsToDocument(settings);
    });
  }

  // System Settings (from Backend)
  fetchSystemSettings(): Observable<SystemSettingsDto> {
    return this.http.get<SystemSettingsDto>(`${this.apiUrl}/system`).pipe(
      tap(settings => this.systemSettingsSignal.set(settings))
    );
  }

  // User Settings (Local Storage)
  updateUserSettings(newSettings: Partial<UserSettings>): void {
    this.userSettingsSignal.update(current => ({
      ...current,
      ...newSettings
    }));
  }

  private loadUserSettings(): UserSettings {
    const saved = localStorage.getItem('autodm_settings');
    if (saved) {
      try {
        return { ...this.defaultSettings, ...JSON.parse(saved) };
      } catch (e) {
        console.error('Failed to parse settings from local storage', e);
      }
    }
    return this.defaultSettings;
  }

  private applySettingsToDocument(settings: UserSettings): void {
    // Example of applying settings to the body or html tag
    document.body.classList.remove('theme-light', 'theme-dark');
    document.body.classList.add(`theme-${settings.theme}`);

    document.body.classList.remove('font-small', 'font-medium', 'font-large');
    document.body.classList.add(`font-${settings.fontSize}`);
  }
}
