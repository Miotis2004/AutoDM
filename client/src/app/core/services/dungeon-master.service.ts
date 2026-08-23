import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SceneInfo, PlayerAction, ActionResponse } from '../models';

@Injectable({
  providedIn: 'root'
})
export class DungeonMasterService {
  private apiUrl = '/api/campaigns';

  constructor(private http: HttpClient) {}

  getCurrentScene(campaignId: number): Observable<SceneInfo> {
    return this.http.get<SceneInfo>(`${this.apiUrl}/${campaignId}/dm/scene`);
  }

  handleAction(campaignId: number, action: PlayerAction): Observable<ActionResponse> {
    return this.http.post<ActionResponse>(`${this.apiUrl}/${campaignId}/dm/actions`, action);
  }
}
