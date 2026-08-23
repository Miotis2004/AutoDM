import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CampaignDto } from '../models';

@Injectable({
  providedIn: 'root'
})
export class CampaignService {
  private apiUrl = '/api/campaigns';

  constructor(private http: HttpClient) {}

  getAllCampaigns(): Observable<CampaignDto[]> {
    return this.http.get<CampaignDto[]>(this.apiUrl);
  }

  getCampaign(id: number): Observable<CampaignDto> {
    return this.http.get<CampaignDto>(`${this.apiUrl}/${id}`);
  }

  createCampaign(campaign: CampaignDto): Observable<CampaignDto> {
    return this.http.post<CampaignDto>(this.apiUrl, campaign);
  }

  updateCampaign(id: number, campaign: CampaignDto): Observable<CampaignDto> {
    return this.http.put<CampaignDto>(`${this.apiUrl}/${id}`, campaign);
  }

  archiveCampaign(id: number): Observable<CampaignDto> {
    return this.http.post<CampaignDto>(`${this.apiUrl}/${id}/archive`, {});
  }

  deleteCampaign(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
