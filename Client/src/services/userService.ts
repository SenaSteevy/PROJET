import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './authService';
import { Role } from 'src/models/Role';
import { FormGroup } from '@angular/forms';
import { User } from 'src/models/User';
import { UserRequest } from 'src/models/UserRequest';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  connectedUser: any;
  connectedUserHasChanged: boolean = false;

  PATH_OF_API = "http://localhost:9090/user-service";



  constructor(private httpClient: HttpClient, private authService: AuthService) {
    this.connectedUser = authService.getUser();
  }

  

  uploadImage(imageData: FormData, username: string, headers: HttpHeaders): Observable<HttpResponse<any>> {
    return this.httpClient.post(this.PATH_OF_API + `/images/upload?username=${username}`, imageData, {
      reportProgress: true,
      observe: 'response'
    });
  }

  deleteImage(id: string) {
    return this.httpClient.post(this.PATH_OF_API + `/images/delete?id=${id}`, null);
  }

  getAllRoles() {
    return this.httpClient.get<Role[]>(this.PATH_OF_API + '/roles/getAll');
  }

  createUser(data: any) {
    return this.httpClient.post(this.PATH_OF_API + '/users/registerNewUser', data);
  }

  updateUser(userId: any, formGroup: FormGroup) {
    return this.httpClient.post<User>(this.PATH_OF_API + '/users/updateUserById/' + userId, formGroup.value);
  }

  public login(loginData: any) {
    return this.httpClient.post("http://localhost:9090/api/authenticate", loginData);
  }

  public newRegisterRequest(formData: any) {
    return this.httpClient.post(this.PATH_OF_API + '/users/newRegisterRequest', formData);
  }

  getAllUsers() {
    return this.httpClient.get<User[]>(this.PATH_OF_API + '/users/getAll');
  }

  deleteUser(userId: string) {
    return this.httpClient.delete(this.PATH_OF_API + "/users/deleteUserById/" + userId);
  }

  getUserById(userId: any) {
    return this.httpClient.get<User>(this.PATH_OF_API + "/users/getById/" + userId);
  }

  getImage(username: string) {
    return this.httpClient.get(this.PATH_OF_API + `/images/getImage?username=${username}`);
  }

  getUserRequests() {
    return this.httpClient.get<UserRequest[]>(`${this.PATH_OF_API}/userRequests/getAll`);
  }

  deleteUserRequest(id: any) {
    return this.httpClient.post(`${this.PATH_OF_API}/userRequests/delete/${id}`,null);
  }
}