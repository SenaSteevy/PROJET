import { Injectable } from '@angular/core';
import { User } from 'src/models/User';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EmailData } from 'src/models/EmailData';
import { Observable, Subscriber, isEmpty, throwError } from 'rxjs';
@Injectable({
    providedIn : 'root'
})

export class UserService{
    
    connectedUser : any;
    PATH_OF_API = "http://localhost:8081/api/user"
    headers = new HttpHeaders()
            .set('No-Auth', 'True');
    
    
    constructor( private httpClient : HttpClient){}

    public login(loginData : any){
        return this.httpClient.post(this.PATH_OF_API + "/authenticate", loginData, { headers : this.headers})
        
    }

      public async newRegisterRequest(formData: any) {
           return this.httpClient.post(this.PATH_OF_API + '/newRegisterRequest', formData, { headers: this.headers })
            };
         
        
      
      
        
}        




