import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { ConfirmDialogComponent } from 'src/app/components/confirm-dialog/confirm-dialog.component';
import { Client } from 'src/models/Client';
import { JobService } from 'src/services/jobService';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css']
})
export class ClientsComponent implements OnInit {
  displayedColumns: string[] = [ 'id','name','address','email','tel', 'actions'];
  clientsDataSource: MatTableDataSource<Client> = new MatTableDataSource<Client>();
  paginatedData: MatTableDataSource<Client> = new MatTableDataSource<Client>();
  @ViewChild(MatPaginator) paginator: any;
  clientList: Client[] = [];

  constructor(private jobService : JobService,
    private dialog : MatDialog,
    private _snackBar : MatSnackBar
    ) { }

  ngOnInit(): void {
    this.getDataSource()
  }

   getDataSource() {
    this.clientsDataSource = new MatTableDataSource<Client>();
    this.paginatedData = new MatTableDataSource<Client>();
    this.jobService.getClients().subscribe({
      next: (response: Client[]) => {
        this.clientsDataSource.data.push(...response);
        this.paginatedData = new MatTableDataSource<Client>( this.clientsDataSource.data.slice(0, 10))
        this.clientList = response
      },
      error: (error: any) => console.log('error during getAllClients:', error),
      complete :  () => {
        this.clientsDataSource.paginator = this.paginator; 
      }
    });
  }

  handlePageEvent(event: PageEvent): void {
    const startIndex = event.pageIndex * event.pageSize;
    const endIndex = startIndex + event.pageSize;
    const paginatedData = this.clientsDataSource.data.slice(startIndex, endIndex);
    this.paginatedData = new MatTableDataSource<Client>(paginatedData);
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
    // Custom filtering function
    this.clientsDataSource.filterPredicate = (client: Client, filter: string) => {
      const id = client.id.toString()
      const name = client.name.toLowerCase();
      const address = client.address.toLowerCase();
      const email = client.email.toLowerCase();      
      const tel = client.tel.toLowerCase(); 
      return (
        id.includes(filter) ||
        name.includes(filter) ||
        address.includes(filter) ||
        email.includes(filter) ||
        tel.includes(filter)
      );
    };
    // Apply the filter
    this.clientsDataSource.filter = filterValue;
    // Reset the paginator to the first page
    if (this.paginator) {
      this.paginator.firstPage();
    }
    // Update the paginatedData with the filtered data
    const startIndex = this.paginator?.pageIndex * this.paginator?.pageSize || 0;
    const endIndex = startIndex + (this.paginator?.pageSize || 10);
    const paginatedData = this.clientsDataSource.filteredData.slice(startIndex, endIndex);
    this.paginatedData = new MatTableDataSource<Client>(paginatedData);
  }

  deleteClient(client : any){
    let dialogRef = this.dialog.open(ConfirmDialogComponent, {

      width: '400px',
      data : { title : "Delete Client ?", content : `Are you sure you want to delete  client ${client.name} ? This will delete all related orders of this clients.`}
    })

    dialogRef.afterClosed().subscribe((result : string) =>{
      if(result=="yes"){

        this.jobService.deleteClient(client.id).subscribe({
          next : (response : any)  => {  
            this.openSnackBar(" Client Deleted.")
            this.getDataSource()
          },
          error : (error: any) => { console.error('Error deleting client', error); } 
        })
      }
    })
  }

  openSnackBar(message : string) {
    this._snackBar.open( message , '', {
      duration: 2000
    });
  }



}
