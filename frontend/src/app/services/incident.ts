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

  private abortController: AbortController | null = null;

  constructor(private http: HttpClient, private zone: NgZone) {
  }

  getAllIncidents(): Observable<IncidentResponse[]> {
    return this.http.get<IncidentResponse[]>(this.baseUrl);
  }

  createIncident(incident: IncidentRequest): Observable<IncidentResponse> {
    return this.http.post<IncidentResponse>(this.baseUrl, incident);
  }

  listenForLiveIncidents(onNewIncident: (incident: IncidentResponse) => void) {
    this.disconnect();

    this.abortController = new AbortController();
    const token = localStorage.getItem('jwt_token');

    fetchEventSource(`${this.baseUrl}/stream`, {
      headers: {
        Authorization: `Bearer ${token}`
      },
      signal: this.abortController.signal,
      onmessage: (ev) => {
        if (ev.event === 'new-incident' || !ev.event) {
          const newIncident: IncidentResponse = JSON.parse(ev.data);

          this.zone.run(() => {
            onNewIncident(newIncident);
          });
        }
      },
      onerror: (err) => {
        console.warn('SSE Stream Error. Throttling reconnect...', err);

        setTimeout(() => {
          console.log('Attempting to rebuild SSE connection...');
          this.listenForLiveIncidents(onNewIncident);
        }, 3000);

        throw err;
      }
    });
  }

  disconnect() {
    if (this.abortController) {
      this.abortController.abort();
      this.abortController = null;
    }
  }
}
