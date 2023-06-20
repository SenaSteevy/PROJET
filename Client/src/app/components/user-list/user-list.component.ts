import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { UserService } from 'src/services/userService';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements  AfterViewInit {

  @ViewChild(MatPaginator, { static: false }) paginator!: MatPaginator;


  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  userList: any[];
  displayedColumns: string[];
  filterText: string;
  filteredUserList: any[];
  dataSource: MatTableDataSource<any>;

  constructor(private userService: UserService, private dialog: MatDialog, private router: Router) {
    this.userList = [];
    this.displayedColumns = ['Profile', 'Name', 'Email', 'Role'];
    this.filterText = '';
    this.filteredUserList = [];
    this.dataSource = new MatTableDataSource<any>([]);

      }

  ngOnInit(): void {
    this.getAllUsers();
  }
  
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  getAllUsers() {
    this.userService.getAllUsers().subscribe(
      (response: any) => {
        console.log(response);
        this.userList = response;
        this.dataSource =  new MatTableDataSource(this.userList);
        this.loadProfileImages();
      },
      (error: any) => {
        console.log(error);
      }
    );
  }

  loadProfileImages() {
    this.userList.forEach(user => {
      this.getImage(user.username).subscribe(
        (res: any) => {
          user.profile = !!res?'data:image/jpeg;base64,' + res.picByte : null;
        },
        error => {
          console.error(error);
        }
      );
    });
  }

  getImage(username: string) {
    return this.userService.getImage(username);
  }


  confirmDelete(user: any) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { user },
      width: '300px'
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'delete') {
        // Call your delete user API here
        this.userService.deleteUser(user.username).subscribe(
          response => {
            console.log('User deleted successfully');
            // Refresh the user list after deletion
            this.getAllUsers();
          },
          error => {
            console.error('Error deleting user', error);
          }
        );
      }
    });
  }

  editUser(user: any) {
    this.router.navigate(['/editUser', user.id]);
  }

  calculateAnimationDelay(index: number): number {
    return (index + 1) * 200; 
  }
  // ...
}
