import { Injectable } from '@angular/core';
import { User } from 'src/models/User';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { EmailData } from 'src/models/EmailData';
import { Observable, Subscriber, isEmpty, throwError } from 'rxjs';
import { AuthService } from './authService';
@Injectable({
    providedIn : 'root'
})

export class UserService{
 
 
    connectedUser : any;
    PATH_OF_API = "http://localhost:8081/api/user"
   
    headers = new HttpHeaders().set('Content-Type', 'application/json').set('Authorization', "Bearer "+this.authService.getToken()  )

    params = new HttpParams().set('username', ' ');
   
    
    constructor( private httpClient : HttpClient, private authService :AuthService){
    }

    public login(loginData : any){
        return this.httpClient.post(this.PATH_OF_API + "/authenticate", loginData, { headers : this.headers})
        
    }

    public  newRegisterRequest(formData: any) {
        return this.httpClient.post(this.PATH_OF_API + '/newRegisterRequest', formData, { headers: this.headers })
    };

    getAllUsers(){
        this.headers = this.headers.set('Authorization', "Bearer "+this.authService.getToken() )
        return this.httpClient.get(this.PATH_OF_API + '/getAll', { headers: this.headers })

    }
    
  onUpload(selectedFile : File, username : string) : string {
    
    //FormData API provides methods and properties to allow us easily prepare form data to be sent with POST HTTP requests.
    const uploadImageData = new FormData();
    uploadImageData.append('imageFile', selectedFile, selectedFile.name);

    this.params =  this.params.set('username', username);
    this.headers = this.headers.set("Authorization","Bearer "+this.authService.getToken())
    //Make a call to the Spring Boot Application to save the image
    this.httpClient.post(this.PATH_OF_API + "/image/upload",uploadImageData, {headers : this.headers , params : this.params} ).subscribe({
        next :  () => { return 'Image uploaded successfully' },
        error : (error)=> { return error.message || "An error occured"} 
        });
        return "An error occured !!"

  }

    //Gets called when the user clicks on retieve image button to get the image from back end
    getImage(username : string) {
    //Make a call to Sprinf Boot to get the Image Bytes.
    this.params = this.params.set('username', username);
    this.headers = this.headers.set("Authorization","Bearer "+this.authService.getToken())

    return this.httpClient.get(this.PATH_OF_API + "/image/getImage" , {headers : this.headers, params :this.params})
      
  }
        
  deleteUser(username : string){
    this.headers = this.headers.set("Authorization","Bearer "+this.authService.getToken())
    return this.httpClient.post(this.PATH_OF_API +"/delete/"+username  , {headers : this.headers})
  }
    
  getUserById(userId: any) {
    this.headers = this.headers.set("Authorization","Bearer "+this.authService.getToken())
    return this.httpClient.get(this.PATH_OF_API+"getById/"+userId, {headers : this.headers})
  
  }
 
    
      
      
        
}        




