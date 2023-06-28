import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Job } from '../models/Job';
import { Planning } from 'src/models/Planning';
import { AuthService } from './authService';
import { Phase } from 'src/models/Phase';

@Injectable({
  providedIn: 'root'
})
export class JobService {
  
  private apiUrl = 'http://localhost:8083/api';

  headers = new HttpHeaders().set('Content-Type', 'application/json').set('Authorization', "Bearer "+this.authService.getToken()  )

  constructor(private http: HttpClient,private authService : AuthService ) {}

  getJobs(): Observable<Job[]> {
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.get<Job[]>(`${this.apiUrl}/ordo/getAllJobs`, { headers: this.headers});
  }

  getAllPlannings(): Observable<Planning[]> {
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.get<Planning[]>(`${this.apiUrl}/ordo/getAllPlannings`);
  }

  getAllPhases() {
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.get<Phase[]>(`${this.apiUrl}/phase/getAllPhases`)
  }
}
