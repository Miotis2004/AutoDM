import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-shell',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <nav>
      <ul>
        <li><a routerLink="/dashboard" routerLinkActive="active">Dashboard</a></li>
        <li><a routerLink="/campaigns" routerLinkActive="active">Campaigns</a></li>
        <li><a routerLink="/play" routerLinkActive="active">Play</a></li>
        <li><a routerLink="/characters" routerLinkActive="active">Characters</a></li>
        <li><a routerLink="/quests" routerLinkActive="active">Quests</a></li>
        <li><a routerLink="/world" routerLinkActive="active">World</a></li>
        <li><a routerLink="/npcs" routerLinkActive="active">NPCs</a></li>
        <li><a routerLink="/encounters" routerLinkActive="active">Encounters</a></li>
        <li><a routerLink="/history" routerLinkActive="active">History</a></li>
        <li><a routerLink="/settings" routerLinkActive="active">Settings</a></li>
      </ul>
    </nav>
    <main>
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [`
    .active {
      font-weight: bold;
    }
    nav ul {
      list-style-type: none;
      padding: 0;
    }
    nav li {
      display: inline;
      margin-right: 10px;
    }
  `]
})
export class AppShellComponent {}
