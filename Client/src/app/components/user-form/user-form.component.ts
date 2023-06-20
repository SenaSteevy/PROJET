import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from 'src/services/userService';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit {

  user : any

  constructor(private userService : UserService, private route :ActivatedRoute ) {}

  ngOnInit(): void {

    this.route.params.subscribe(params => {
      const userId = params['id'];

      this.userService.getUserById(userId).subscribe({

        next : (response) => { this.user = response},
        error : (error) => { console.log(error)}
      })
        
      
  
      
    });

  }

}
