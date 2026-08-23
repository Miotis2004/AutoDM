import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NpcDto } from '../models';

@Injectable({
  providedIn: 'root'
})
export class NpcService {
  private apiUrl = '/api';

  constructor(private http: HttpClient) {}

  getNpcsByCampaign(campaignId: number): Observable<NpcDto[]> {
    return this.http.get<NpcDto[]>(`${this.apiUrl}/campaigns/${campaignId}/npcs`);
  }

  getNpcsByLocation(locationId: number): Observable<NpcDto[]> {
    return this.http.get<NpcDto[]>(`${this.apiUrl}/locations/${locationId}/npcs`);
  }

  getNpc(id: number): Observable<NpcDto> {
    return this.http.get<NpcDto>(`${this.apiUrl}/npcs/${id}`);
  }

  createNpc(campaignId: number, npc: NpcDto): Observable<NpcDto> {
    return this.http.post<NpcDto>(`${this.apiUrl}/campaigns/${campaignId}/npcs`, npc);
  }

  updateNpc(id: number, npc: NpcDto): Observable<NpcDto> {
    return this.http.put<NpcDto>(`${this.apiUrl}/npcs/${id}`, npc);
  }

  deleteNpc(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/npcs/${id}`);
  }
}
