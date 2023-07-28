import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, takeLast } from 'rxjs';
import { Job } from '../models/Job';
import { Planning } from 'src/models/Planning';
import { AuthService } from './authService';
import { Phase } from 'src/models/Phase';
import { Task } from 'src/models/Task';
import { Client } from 'src/models/Client';
import { Resource } from 'src/models/Resource';
import { Treatment } from 'src/models/Treatment';
import { UserRequest } from 'src/models/UserRequest';

@Injectable({
  providedIn: 'root'
})
export class JobService {

  phaseList : Phase[] = []
  clientlist : Client[] = []
  treatmentList : Treatment[] = []
  resourceList : Resource[] = []

  async updateJobService(): Promise<void> {
    try {
      const clientsResponse = await this.getAllClients().toPromise();
      this.clientlist = clientsResponse || [];
  
      const phasesResponse = await this.getAllPhases().toPromise();
      this.phaseList = phasesResponse || [];
  
      const treatmentsResponse = await this.getTreatments().toPromise();
      this.treatmentList = treatmentsResponse || [];
  
      const resourcesResponse = await this.getAllResources().toPromise();
      this.resourceList = resourcesResponse || [];

    } catch (error) {
      console.error('Error while fetching data:', error);
    }
  }
  
  

  private apiUrl = 'http://localhost:8083/api';

  headers = new HttpHeaders().set('Content-Type', 'application/json').set('Authorization', "Bearer "+this.authService.getToken()  )

  constructor(private http: HttpClient,private authService : AuthService ) {
     this.updateJobService()
  }

  getTreatments() {
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.get<Treatment[]>(`${this.apiUrl}/treatments/getAllTreatments`, { headers: this.headers});
  }

  deleteTreatment(id : any){
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.post(`${this.apiUrl}/treatments/deleteTreatmentById/${id}`, { headers: this.headers});

  }

  getTreatmentById(id:any){
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.get<Treatment>(`${this.apiUrl}/treatments/getTreatmentById/${id}`, { headers: this.headers});

  }

  updateTreatment(id : any, data: any){
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.post<Treatment>(`${this.apiUrl}/treatments/updateTreatmentById/${id}`,data, { headers: this.headers});

  }

  createTreatment(data : any){
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.post<Treatment>(`${this.apiUrl}/treatments/saveTreatment`,data, { headers: this.headers});
  }
  
 
  getJobs(): Observable<Job[]> {
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.get<Job[]>(`${this.apiUrl}/ordo/getAllJobs`, { headers: this.headers});
  }

  getAllPlannings(): Observable<Planning[]> {
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.get<Planning[]>(`${this.apiUrl}/ordo/getAllPlannings`, { headers : this.headers});
  }

  getAllPhases() {
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.get<Phase[]>(`${this.apiUrl}/phases/getAllPhases`, { headers : this.headers})
  }

   makeNewPlanning()  {
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.get<Planning>(`${this.apiUrl}/ordo/solve`, { headers : this.headers})
  }

  updateJobList(jobList: Job[]) {
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.post<Job[]>(`${this.apiUrl}/ordo/updateJobList`,jobList, { headers : this.headers})
  }

  getAllClients() {
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.get<Client[]>(`${this.apiUrl}/clients/getAllClients`, { headers : this.headers})
  }

  getAllResources() {
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.http.get<Resource[]>(`${this.apiUrl}/resources/getAllResources`, { headers : this.headers})
    }

    newJob(job : Job){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.post(`${this.apiUrl}/ordo/createJob`,job, { headers : this.headers})  
    }
  
    deleteJob(numOrder : number){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.post(`${this.apiUrl}/ordo/deleteJobById/${numOrder}`, { headers : this.headers}) 
    }

    getJobById(id : string){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.get<Job>(`${this.apiUrl}/ordo/getJobById/${id}`, { headers : this.headers}) 
    }
    updateJob(numOrder : number, job : any){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.post(`${this.apiUrl}/ordo/updateJobById/${numOrder}`, job,  { headers : this.headers}) 
    }

    getClients(){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.get<Client[]>(`${this.apiUrl}/clients/getAllClients`,  { headers : this.headers}) 
    }

    getClientById(id : string){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.get<Client>(`${this.apiUrl}/clients/getClientById/${id}`,  { headers : this.headers}) 
    }

    updateClient(id : number, data : any){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.post<Client>(`${this.apiUrl}/clients/updateClientById/${id}`,data,  { headers : this.headers}) 
    }

    createClient(data : any){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.post<Client>(`${this.apiUrl}/clients/saveClient`,data,  { headers : this.headers}) 
    }

    deleteClient(id : number){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.post(`${this.apiUrl}/clients/deleteClientById/${id}`,  { headers : this.headers}) 
    }

    getResources(){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.get<Resource[]>(`${this.apiUrl}/resources/getAllResources`,  { headers : this.headers}) 
    }

    getResourceById(id : string){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.get<Resource>(`${this.apiUrl}/resources/getResourceById/${id}`,  { headers : this.headers}) 
    }

    updateResource(id : number, data : any){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.post<Resource>(`${this.apiUrl}/resources/updateResourceById/${id}`,data,  { headers : this.headers}) 
    }

    createResource(data : any){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.post<Resource>(`${this.apiUrl}/resources/saveResource`,data,  { headers : this.headers}) 
    }

    deleteResource(id : number){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.post(`${this.apiUrl}/resources/deleteResourceById/${id}`,  { headers : this.headers}) 
    }

    getPhaseById(id:any){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.get<Phase>(`${this.apiUrl}/phases/getPhaseById/${id}`,  { headers : this.headers}) 
    }
    updatePhase(id : number, data : any){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.post<Phase>(`${this.apiUrl}/phases/updatePhaseById/${id}`,data,  { headers : this.headers}) 
    }

    createPhase(data : any){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.post<Phase>(`${this.apiUrl}/phases/savePhase`,data,  { headers : this.headers}) 
    }

    deletePhase(id : number){
      this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
      return this.http.post(`${this.apiUrl}/phases/deletePhaseById/${id}`,  { headers : this.headers}) 
    }

}


 
 
 

