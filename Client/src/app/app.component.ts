import { Component, DoCheck } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/services/authService';

@Component({
  selector: 'app-root',
  
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements DoCheck{

  connectedUser : any
  constructor(private authService : AuthService, private router : Router){
    this.connectedUser = this.authService.getUser();
  }
  ngDoCheck(): void {
    const user = this.authService.getUser()
    if (user != undefined) {
      this.connectedUser = user;
    } 

  }
  

  Disconnect() : void {
    this.connectedUser = undefined;
    this.authService.clear();
    this.router.navigate(["/login"]);
  }
}
