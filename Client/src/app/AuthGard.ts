import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from 'src/services/authService';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (this.authService.isLoggedIn()) {
      console.log("authgard : ",this.authService.getUser())
      return true;
    } else {
      this.router.navigate(['/login']);
      console.log("authGard : no user")
      return false;
    }
  }
}
