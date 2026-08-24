import { Routes } from '@angular/router';
import { AppShellComponent } from './layout/app-shell/app-shell.component';

export const routes: Routes = [
  {
    path: '',
    component: AppShellComponent,
    children: [
      { path: 'dashboard', loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent), title: 'Dashboard - AutoDM' },
      { path: 'campaigns', loadComponent: () => import('./features/campaigns/campaigns.component').then(m => m.CampaignsComponent), title: 'Campaigns - AutoDM' },
      { path: 'play', loadComponent: () => import('./features/play/play.component').then(m => m.PlayComponent), title: 'Play - AutoDM' },
      { path: 'characters', loadComponent: () => import('./features/characters/characters.component').then(m => m.CharactersComponent), title: 'Characters - AutoDM' },
      { path: 'quests', loadComponent: () => import('./features/quests/quests.component').then(m => m.QuestsComponent), title: 'Quests - AutoDM' },
      { path: 'world', loadComponent: () => import('./features/world/world.component').then(m => m.WorldComponent), title: 'World - AutoDM' },
      { path: 'npcs', loadComponent: () => import('./features/npcs/npcs.component').then(m => m.NpcsComponent), title: 'NPCs - AutoDM' },
      { path: 'items', loadComponent: () => import('./features/items/items.component').then(m => m.ItemsComponent), title: 'Items - AutoDM' },
      { path: 'creatures', loadComponent: () => import('./features/creatures/creatures.component').then(m => m.CreaturesComponent), title: 'Creatures - AutoDM' },
      { path: 'encounters', loadComponent: () => import('./features/encounters/encounters.component').then(m => m.EncountersComponent), title: 'Encounters - AutoDM' },
      { path: 'history', loadComponent: () => import('./features/history/history.component').then(m => m.HistoryComponent), title: 'History - AutoDM' },
      { path: 'settings', loadComponent: () => import('./features/settings/settings.component').then(m => m.SettingsComponent), title: 'Settings - AutoDM' },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  }
];
