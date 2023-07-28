import { Component, DoCheck } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { FileHandle } from 'src/models/FileHandle';
import { AuthService } from 'src/services/authService';
import { UserService } from 'src/services/userService';

@Component({
  selector: 'app-root',
  
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements DoCheck{
  menus = [
    {
      icon: 'home',
      label: 'Home',
      items: [ ]
    },
    {
      icon: 'date_range',
      label: 'Plannings',
      items: [
        {
          routerLink: '/schedules',
          icon: 'list_alt',
          label: 'All Plannings'
        },
        {
          routerLink: '/schedules/new',
          icon: 'add',
          label: 'New Planning'
        }
      ]
    },
    {
      icon : 'group',
      label : 'Users',
      items :[
        {
        routerLink: '/users',
        icon: 'list_alt',
        label: 'All users'

        },
        {
          routerLink: '/users/new',
          icon: 'person_add',
          label: 'Add new user'
  
          },
          {
            routerLink: '/roles',
            icon: 'accessibility',
            label: 'Roles Details'
            },
            {
              routerLink: '/register-requests',
              icon: 'contacts',
              label: 'Register Requests'
              }
      ]
    },
    {
      icon : 'contacts',
      label : 'Clients',
      items :[
        {
        routerLink: '/clients',
        icon: 'list_alt',
        label: 'All Clients'
        },
        {
          routerLink: '/clients/new',
          icon: 'add',
          label: 'Add new clients'
          }
      ]
    },
    {
      icon : 'receipt',
      label : 'Orders',
      items :[
        {
        routerLink: '/orders',
        icon: 'list',
        label: 'All Orders'
        },
        {
          routerLink: '/orders/new',
          icon: 'add',
          label: 'New Order'
          }
      ]
    },
    {
      icon : 'domain',
      label : 'Stocks',
      items :[
        {
        routerLink: '/resources',
        icon: 'list_alt',
        label: 'Resources in stocks'
        },
        {
          routerLink: '/resources/new',
          icon: 'add',
          label: 'New Stock'
          }
      ]
    },
    {
      icon : 'factory',
      label : 'Production Line',
      items :[
        {
        routerLink: '/production-line',
        icon: 'settings_input_component',
        label: 'Production phases'
        },
        {
          routerLink: '/production-line/phases/new',
          icon: 'add',
          label: 'Add New Phase'
          },
        {
          routerLink: 'production-line/treatments',
          icon: 'widgets',
          label: 'ALL Treatments'
          },{
            routerLink: 'production-line/treatments/new',
            icon: 'add',
            label: 'Add New Treatments'
            }
      ]
    },
    {
      icon : 'settings',
      label : 'Settings',
      items : []
    }
  ];
  

  connectedUser : any
  constructor(private authService : AuthService, 
    private router : Router, 
    private userService : UserService,
    private sanitizer : DomSanitizer){
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

  isActive(route: string): boolean {
    return this.router.isActive(route, true);
  }

 
}
