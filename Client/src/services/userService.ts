import { Injectable } from '@angular/core';
import { User } from 'src/models/User';
import { HttpClient, HttpEvent, HttpEventType, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { EmailData } from 'src/models/EmailData';
import { Observable, Subscriber, isEmpty, map, throwError } from 'rxjs';
import { AuthService } from './authService';
import { Role } from 'src/models/Role';
import { FormGroup } from '@angular/forms';
import { UserRequest } from 'src/models/UserRequest';
@Injectable({
    providedIn : 'root'
})

export class UserService{

  connectedUser : any;
    
  PATH_OF_API = "http://localhost:8081/api/user"
 
  headers = new HttpHeaders().set('Content-Type', 'application/json').set('Authorization', "Bearer "+this.authService.getToken()  )

  params = new HttpParams().set('username', ' ');

  constructor( private httpClient : HttpClient, private authService :AuthService){
    this.connectedUser = authService.getUser()
  }
 
  

  uploadImage(imageData: FormData, username: string, headers: HttpHeaders): Observable<HttpResponse<any>> {
    return this.httpClient.post(`http://localhost:8081/api/user/image/upload?username=${username}`, imageData, {
      headers: headers,
      reportProgress: true,
      observe: 'response' // Change observe option to 'response'
    });
  }
  
  

  getAllRoles() {
    this.headers = this.headers.set('Authorization', "Bearer "+this.authService.getToken() )
    return this.httpClient.get<Role[]>(this.PATH_OF_API + '/getAllRoles', { headers: this.headers })
    
  }

  createUser(data: any) {
    this.headers = this.headers.set('Authorization', "Bearer "+this.authService.getToken() )
    return this.httpClient.post(this.PATH_OF_API + '/registerNewUser',data, { headers: this.headers })
    
  }

  updateUser(userId :any, formGroup: FormGroup) {
    this.headers = this.headers.set('Authorization', "Bearer "+this.authService.getToken() )
    return this.httpClient.post<User>(this.PATH_OF_API + '/updateUserById/'+userId, formGroup.value, { headers: this.headers, observe : 'response' }) 
     }


    public login(loginData : any){
        return this.httpClient.post(this.PATH_OF_API + "/authenticate", loginData, { headers : this.headers})
        
    }

    public  newRegisterRequest(formData: any) {
        return this.httpClient.post(this.PATH_OF_API + '/newRegisterRequest', formData, { headers: this.headers })
    };

    getAllUsers(){
        this.headers = this.headers.set('Authorization', "Bearer "+this.authService.getToken() )
        return this.httpClient.get<User[]>(this.PATH_OF_API + '/getAll', { headers: this.headers })

    }
    
        
  deleteUser(userId : string){
    this.headers = this.headers.set("Authorization","Bearer "+this.authService.getToken())
    return this.httpClient.delete(this.PATH_OF_API +"/deleteUserById/"+userId  , {headers : this.headers})
  }
    
  getUserById(userId: any) {
    this.headers = this.headers.set("Authorization","Bearer "+this.authService.getToken())
    return this.httpClient.get<User>(this.PATH_OF_API+"/getById/"+userId, {headers : this.headers})
  
  }

  getImage(username : string){
    this.headers = this.headers.set("Authorization","Bearer "+this.authService.getToken())
    return this.httpClient.get(this.PATH_OF_API+`/image/getImage?username=${username}`, {headers : this.headers})
  }
 
    
  getUserRequests(){
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.httpClient.get<UserRequest[]>(`${this.PATH_OF_API}/getAllUserRequests`,  { headers : this.headers}) 
  }

  deleteUserRequest(id : any){
    this.headers.set('Authorization', "Bearer "+this.authService.getToken()  )
    return this.httpClient.post(`${this.PATH_OF_API}/deleteUserRequest/${id}`, { headers : this.headers}) 
  }
      
      
        
}        




