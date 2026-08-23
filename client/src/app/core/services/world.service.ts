import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LocationDto, FactionDto } from '../models';

@Injectable({
  providedIn: 'root'
})
export class WorldService {
  private apiUrl = '/api';

  constructor(private http: HttpClient) {}

  // Locations
  getLocationsByCampaign(campaignId: number): Observable<LocationDto[]> {
    return this.http.get<LocationDto[]>(`${this.apiUrl}/campaigns/${campaignId}/locations`);
  }

  getLocation(id: number): Observable<LocationDto> {
    return this.http.get<LocationDto>(`${this.apiUrl}/locations/${id}`);
  }

  createLocation(campaignId: number, location: LocationDto): Observable<LocationDto> {
    return this.http.post<LocationDto>(`${this.apiUrl}/campaigns/${campaignId}/locations`, location);
  }

  updateLocation(id: number, location: LocationDto): Observable<LocationDto> {
    return this.http.put<LocationDto>(`${this.apiUrl}/locations/${id}`, location);
  }

  deleteLocation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/locations/${id}`);
  }

  // Factions
  getFactionsByCampaign(campaignId: number): Observable<FactionDto[]> {
    return this.http.get<FactionDto[]>(`${this.apiUrl}/campaigns/${campaignId}/factions`);
  }

  getFaction(id: number): Observable<FactionDto> {
    return this.http.get<FactionDto>(`${this.apiUrl}/factions/${id}`);
  }

  createFaction(campaignId: number, faction: FactionDto): Observable<FactionDto> {
    return this.http.post<FactionDto>(`${this.apiUrl}/campaigns/${campaignId}/factions`, faction);
  }

  updateFaction(id: number, faction: FactionDto): Observable<FactionDto> {
    return this.http.put<FactionDto>(`${this.apiUrl}/factions/${id}`, faction);
  }

  deleteFaction(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/factions/${id}`);
  }
}
