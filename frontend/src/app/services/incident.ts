import { Injectable, NgZone } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { type IncidentRequest, type IncidentResponse } from '../models/incident';
import { Observable } from 'rxjs';
import { fetchEventSource } from '@microsoft/fetch-event-source';

@Injectable({
  providedIn: 'root'
})
export class IncidentService {
  private baseUrl = 'http://localhost:8080/api/incidents';

  constructor(private http: HttpClient, private zone: NgZone) {
  }

  getAllIncidents(): Observable<IncidentResponse[]> {
    return this.http.get<IncidentResponse[]>(this.baseUrl);
  }

  createIncident(incident: IncidentRequest): Observable<IncidentResponse> {
    return this.http.post<IncidentResponse>(this.baseUrl, incident);
  }

  listenForLiveIncidents(onNewIncident: (incident: IncidentResponse) => void) {
    const token = localStorage.getItem('jwt_token');

    fetchEventSource(`${this.baseUrl}/stream`, {
      headers: {
        Authorization: `Bearer ${token}`
      },
      onmessage: (ev) => {
        if (ev.event === 'new-incident') {
          const newIncident: IncidentResponse = JSON.parse(ev.data);

          this.zone.run(() => {
            onNewIncident(newIncident);
          });
        }
      },
      onerror(err) {
        console.error('SSE Stream Error:', err);
      }
    });
  }
}
