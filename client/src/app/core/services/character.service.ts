import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PlayerCharacterDto } from '../models';

@Injectable({
  providedIn: 'root'
})
export class CharacterService {
  private apiUrl = '/api';

  constructor(private http: HttpClient) {}

  getCharactersByCampaign(campaignId: number): Observable<PlayerCharacterDto[]> {
    return this.http.get<PlayerCharacterDto[]>(`${this.apiUrl}/campaigns/${campaignId}/characters`);
  }

  getCharacter(id: number): Observable<PlayerCharacterDto> {
    return this.http.get<PlayerCharacterDto>(`${this.apiUrl}/characters/${id}`);
  }

  createCharacter(campaignId: number, character: PlayerCharacterDto): Observable<PlayerCharacterDto> {
    return this.http.post<PlayerCharacterDto>(`${this.apiUrl}/campaigns/${campaignId}/characters`, character);
  }

  updateCharacter(id: number, character: PlayerCharacterDto): Observable<PlayerCharacterDto> {
    return this.http.put<PlayerCharacterDto>(`${this.apiUrl}/characters/${id}`, character);
  }

  deleteCharacter(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/characters/${id}`);
  }
}
