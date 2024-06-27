import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { ConfirmDialogComponent } from 'src/app/components/confirm-dialog/confirm-dialog.component';
import { User } from 'src/models/User';
import { UserRequest } from 'src/models/UserRequest';
import { JobService } from 'src/services/jobService';
import { UserService } from 'src/services/userService';

@Component({
  selector: 'app-user-requests',
  templateUrl: './user-requests.component.html',
  styleUrls: ['./user-requests.component.css']
})
export class UserRequestsComponent implements OnInit {

  displayedColumns: string[] = [ 'id','name','email','post', 'actions'];
  userRequestsDataSource: MatTableDataSource<UserRequest> = new MatTableDataSource<UserRequest>();
  paginatedData: MatTableDataSource<UserRequest> = new MatTableDataSource<UserRequest>();
  @ViewChild(MatPaginator) paginator: any;
  userRequestList: UserRequest[] = [];

  constructor(private jobService : JobService,
    private dialog : MatDialog,
    private _snackBar : MatSnackBar,
    private userService : UserService,
    private router : Router
    ) { }

  ngOnInit(): void {
    this.getDataSource()
  }

   getDataSource() {
    this.userRequestsDataSource = new MatTableDataSource<UserRequest>();
    this.paginatedData = new MatTableDataSource<UserRequest>();
    this.userService.getUserRequests().subscribe({
      next: (response: UserRequest[]) => {
        this.userRequestsDataSource.data.push(...response);
        this.paginatedData = new MatTableDataSource<UserRequest>( this.userRequestsDataSource.data.slice(0, 10))
        this.userRequestList = response
      },
      error: (error: any) => console.log('error during getAllUserRequests:', error),
      complete :  () => {
        this.userRequestsDataSource.paginator = this.paginator; 
      }
    });
  }

  handlePageEvent(event: PageEvent): void {
    const startIndex = event.pageIndex * event.pageSize;
    const endIndex = startIndex + event.pageSize;
    const paginatedData = this.userRequestsDataSource.data.slice(startIndex, endIndex);
    this.paginatedData = new MatTableDataSource<UserRequest>(paginatedData);
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
    // Custom filtering function
    this.userRequestsDataSource.filterPredicate = (userRequest: UserRequest, filter: string) => {
      const id = userRequest.id.toString()
      const firstName = userRequest.firstName.toLowerCase();
      const lastName = userRequest.lastName.toLowerCase();
      const email = userRequest.email.toLowerCase();      
      const gender = userRequest.gender.toLowerCase(); 
      const post = userRequest.post.toLowerCase(); 
      const status = userRequest.status.toLowerCase(); 
      return (
        id.includes(filter) ||
        firstName.includes(filter) ||
        lastName.includes(filter) ||
        email.includes(filter) ||
        gender.includes(filter) ||
        post.includes(filter) ||
        status.includes(filter)
      );
    };
    // Apply the filter
    this.userRequestsDataSource.filter = filterValue;
    // Reset the paginator to the first page
    if (this.paginator) {
      this.paginator.firstPage();
    }
    // Update the paginatedData with the filtered data
    const startIndex = this.paginator?.pageIndex * this.paginator?.pageSize || 0;
    const endIndex = startIndex + (this.paginator?.pageSize || 10);
    const paginatedData = this.userRequestsDataSource.filteredData.slice(startIndex, endIndex);
    this.paginatedData = new MatTableDataSource<UserRequest>(paginatedData);
  }

  deleteUserRequest(userRequest : UserRequest){
    let dialogRef = this.dialog.open(ConfirmDialogComponent, {

      width: '400px',
      data : { title : "Delete UserRequest ?", content : `Are you sure you want to delete this register request ?`}
    })

    dialogRef.afterClosed().subscribe((result : string) =>{
      if(result=="yes"){

        console.log("userRequest id : ",userRequest.id)

        this.userService.deleteUserRequest(userRequest.id).subscribe({
          next : (response : any)  => {  
            this.openSnackBar(" UserRequest Deleted.")
            this.getDataSource()
          },
          error : (error: any) => { console.error('Error deleting userRequest', error); } 
        })
      }
    })
  }

  openSnackBar(message : string) {
    this._snackBar.open( message , '', {
      duration: 2000
    });
  }

  acceptUser(userRequest : UserRequest){

     const user : User = {
       id: "",
       email: userRequest.email,
       gender: userRequest.gender,
       firstName: userRequest.firstName,
       lastName: userRequest.lastName,
       post: userRequest.post,
       role: [],
       profile: null,
       password: ''
     }

     this.userService.createUser(user).subscribe({
      next : (response : any) => {
        this.router.navigate(["users/edit/response.id"])
      }
     })
  }


}
