import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { AuthGuard } from './AuthGard';
import { HomeComponent } from './pages/home/home.component';
import { UsersComponent } from './pages/users/users.component';
import { StocksComponent } from './pages/stocks/stocks.component';
import { ScheduleComponent } from './pages/schedule/schedule.component';
import { AppComponent } from './app.component';


const routes: Routes = [
  { path : "login", pathMatch : "full", component : LoginComponent},
  { path : "home", pathMatch : "full", component : HomeComponent, canActivate : [AuthGuard]},
  { path : "users", pathMatch : "full", component : UsersComponent, canActivate : [AuthGuard]},
  { path : "stocks", pathMatch : "full", component : StocksComponent, canActivate : [AuthGuard]},
  { path : "schedule", pathMatch : "full", component : ScheduleComponent, canActivate : [AuthGuard]},
  { path : "", pathMatch: 'full', redirectTo:"/home"},
  { path : "", component : AppComponent , canActivate : [AuthGuard] }


]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
