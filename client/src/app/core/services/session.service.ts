import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GameSessionDto, CampaignEventDto } from '../models';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private apiUrl = '/api';

  constructor(private http: HttpClient) {}

  startSession(campaignId: number): Observable<GameSessionDto> {
    return this.http.post<GameSessionDto>(`${this.apiUrl}/campaigns/${campaignId}/sessions/start`, {});
  }

  resumeSession(campaignId: number): Observable<GameSessionDto> {
    return this.http.post<GameSessionDto>(`${this.apiUrl}/campaigns/${campaignId}/sessions/resume`, {});
  }

  endSession(sessionId: number): Observable<GameSessionDto> {
    return this.http.post<GameSessionDto>(`${this.apiUrl}/sessions/${sessionId}/end`, {});
  }

  getCampaignSessions(campaignId: number): Observable<GameSessionDto[]> {
    return this.http.get<GameSessionDto[]>(`${this.apiUrl}/campaigns/${campaignId}/sessions`);
  }

  getCampaignEvents(campaignId: number): Observable<CampaignEventDto[]> {
    return this.http.get<CampaignEventDto[]>(`${this.apiUrl}/campaigns/${campaignId}/events`);
  }
}
