export interface IncidentRequest {
  latitude: number;
  longitude: number;
  category: string;
  severityScore: number;
  incidentTime: string;
}

export interface IncidentResponse {
  id: number;
  latitude: number;
  longitude: number;
  category: string;
  severityScore: number;
  incidentTime: string;
}
