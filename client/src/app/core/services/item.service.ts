import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ItemDto } from '../models';

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  private apiUrl = '/api/items';

  constructor(private http: HttpClient) {}

  getItemsByCampaign(campaignId: number): Observable<ItemDto[]> {
    return this.http.get<ItemDto[]>(`${this.apiUrl}/campaign/${campaignId}`);
  }

  getItemsByCharacter(characterId: number): Observable<ItemDto[]> {
    return this.http.get<ItemDto[]>(`${this.apiUrl}/character/${characterId}`);
  }

  getItem(id: number): Observable<ItemDto> {
    return this.http.get<ItemDto>(`${this.apiUrl}/${id}`);
  }

  createItem(item: ItemDto): Observable<ItemDto> {
    return this.http.post<ItemDto>(this.apiUrl, item);
  }

  updateItem(id: number, item: ItemDto): Observable<ItemDto> {
    return this.http.put<ItemDto>(`${this.apiUrl}/${id}`, item);
  }

  deleteItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  transferItem(id: number, targetCharacterId: number): Observable<ItemDto> {
    return this.http.post<ItemDto>(`${this.apiUrl}/${id}/transfer`, { targetCharacterId });
  }
}
