import { ChangeDetectorRef, Component, NgZone, OnDestroy, OnInit } from '@angular/core';
import { IncidentRequest, IncidentResponse } from '../models/incident';
import { IncidentService } from '../services/incident';
import * as L from 'leaflet';
import { FormsModule } from '@angular/forms';
import { CommonModule, DecimalPipe, formatDate } from '@angular/common';
import { Router } from '@angular/router';
import { Inject, LOCALE_ID } from '@angular/core';

const iconRetinaUrl = 'assets/marker-icon-2x.png';
const iconUrl = 'assets/marker-icon.png';
const shadowUrl = 'assets/marker-shadow.png';
const iconDefault = L.icon({
  iconRetinaUrl,
  iconUrl,
  shadowUrl,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41],
});
L.Marker.prototype.options.icon = iconDefault;

@Component({
  selector: 'app-map',
  imports: [FormsModule, DecimalPipe, CommonModule],
  templateUrl: './map.component.html',
  styleUrl: './map.component.css',
})
export class MapComponent implements OnInit, OnDestroy {
  private map: L.Map | undefined;

  isReporting = false;
  isSubmitting = false;
  selectedLat = 0;
  selectedLng = 0;

  newCategory = 'THEFT';
  newSeverity = 1;
  newIncidentTime = '';
  maxDateTime = '';

  private activeMarkers = new Map<number, L.Marker>();

  private activeTimers = new Map<number, any>();

  constructor(
    private incidentService: IncidentService,
    private zone: NgZone,
    private cdr: ChangeDetectorRef,
    private router: Router,
    @Inject(LOCALE_ID) private locale: string,
  ) {}

  ngOnInit(): void {
    this.initMap();
    this.loadIncidents();
    this.incidentService.listenForLiveIncidents((liveIncident) => {
      this.addPinToMap(liveIncident);
    });
  }

  ngOnDestroy(): void {
    this.activeTimers.forEach((timerId) => clearTimeout(timerId));
    this.activeTimers.clear();
    this.incidentService.disconnect();
  }

  private initMap(): void {
    const southWest = L.latLng(51.15, 22.4);
    const northEast = L.latLng(51.32, 22.7);
    const lublinBounds = L.latLngBounds(southWest, northEast);

    this.map = L.map('map', {
      maxBounds: lublinBounds,
      maxBoundsViscosity: 0.7,
      minZoom: 11,
    }).setView([51.2465, 22.5684], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
    }).addTo(this.map);

    this.map.on('click', (e: L.LeafletMouseEvent) => {
      this.zone.run(() => {
        this.selectedLat = e.latlng.lat;
        this.selectedLng = e.latlng.lng;
        const rightNow = this.getCurrentDateTimeLocal();
        this.newIncidentTime = rightNow;
        this.maxDateTime = rightNow;
        this.isReporting = true;

        this.cdr.detectChanges();
      });
    });
  }

  private loadIncidents(): void {
    this.incidentService.getAllIncidents().subscribe({
      next: (incidents: IncidentResponse[]) => {
        incidents.forEach((incident) => {
          this.addPinToMap(incident);
        });
      },
      error: (error) => {
        console.error('Failed to load incidents', error);
      },
    });
  }

  private addPinToMap(incident: IncidentResponse): void {
    if (!this.map) return;

    if (this.activeMarkers.has(incident.id)) {
      return;
    }

    const creationTime = new Date(incident.incidentTime).getTime();
    const fifteenMinutesInMs = 15 * 60 * 1000;
    const expirationTime = creationTime + fifteenMinutesInMs;
    const timeRemaining = expirationTime - Date.now();

    if (timeRemaining <= 0) return;

    const marker = L.marker([incident.latitude, incident.longitude]).addTo(this.map);
    const formattedTime = formatDate(incident.incidentTime, 'dd.MM.yyyy HH:mm', this.locale);
    marker.bindPopup(`
        <strong>${incident.category}</strong><br>
        <p>Happened at ${formattedTime}</p>
        `);

    this.activeMarkers.set(incident.id, marker);

    const timerId = setTimeout(() => {
      this.zone.run(() => {
        this.removePinFromMap(incident.id);
      });
    }, timeRemaining);

    this.activeTimers.set(incident.id, timerId);
  }

  private removePinFromMap(incidentId: number): void {
    const marker = this.activeMarkers.get(incidentId);

    if (marker && this.map) {
      this.map.removeLayer(marker);

      this.activeMarkers.delete(incidentId);
      this.activeTimers.delete(incidentId);
    }
  }

  cancelReport() {
    this.isReporting = false;
  }

  submitReport() {
    if (this.isSubmitting) return;

    this.isSubmitting = true;

    const newIncident: IncidentRequest = {
      latitude: this.selectedLat,
      longitude: this.selectedLng,
      category: this.newCategory,
      severityScore: this.newSeverity,
      incidentTime: this.newIncidentTime,
    };

    this.incidentService.createIncident(newIncident).subscribe({
      next: (response) => {
        this.addPinToMap(response);

        this.isReporting = false;
        this.newSeverity = 1;
        this.newCategory = 'THEFT';
        this.isSubmitting = false;

        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Failed to save pin:', error);
      },
    });
  }

  private getCurrentDateTimeLocal(): string {
    const now = new Date();
    const offset = now.getTimezoneOffset() * 60000;
    const localIsoTime = new Date(now.getTime() - offset).toISOString().slice(0, 16);
    return localIsoTime;
  }

  logout(): void {
    localStorage.removeItem('jwt_token');
    this.incidentService.disconnect();

    this.router.navigate(['/login']);
  }
}
