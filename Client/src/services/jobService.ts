import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Job } from '../models/Job';
import { Planning } from 'src/models/Planning';

@Injectable({
  providedIn: 'root'
})
export class JobService {
  private apiUrl = 'http://localhost:8083/api/ordo';

  constructor(private http: HttpClient) {}

  getJobs(): Observable<Job[]> {
    return this.http.get<Job[]>(`${this.apiUrl}/getAllJobs`);
  }

  getAllPlannings(): Observable<Planning[]> {
    return this.http.get<Planning[]>(`${this.apiUrl}/getAllPlannings`);
  }
}
