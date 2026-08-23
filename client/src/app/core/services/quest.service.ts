import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { QuestDto, QuestStatus, ObjectiveDto } from '../models';

@Injectable({
  providedIn: 'root'
})
export class QuestService {
  private apiUrl = '/api';

  constructor(private http: HttpClient) {}

  getQuestsByCampaign(campaignId: number): Observable<QuestDto[]> {
    return this.http.get<QuestDto[]>(`${this.apiUrl}/campaigns/${campaignId}/quests`);
  }

  getQuest(id: number): Observable<QuestDto> {
    return this.http.get<QuestDto>(`${this.apiUrl}/quests/${id}`);
  }

  createQuest(campaignId: number, quest: QuestDto): Observable<QuestDto> {
    return this.http.post<QuestDto>(`${this.apiUrl}/campaigns/${campaignId}/quests`, quest);
  }

  updateQuest(id: number, quest: QuestDto): Observable<QuestDto> {
    return this.http.put<QuestDto>(`${this.apiUrl}/quests/${id}`, quest);
  }

  deleteQuest(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/quests/${id}`);
  }

  updateQuestStatus(id: number, status: QuestStatus): Observable<QuestDto> {
    return this.http.put<QuestDto>(`${this.apiUrl}/quests/${id}/status`, { status });
  }

  completeObjective(id: number): Observable<ObjectiveDto> {
    return this.http.put<ObjectiveDto>(`${this.apiUrl}/objectives/${id}/complete`, {});
  }
}
