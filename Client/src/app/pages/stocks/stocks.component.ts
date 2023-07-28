import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { ConfirmDialogComponent } from 'src/app/components/confirm-dialog/confirm-dialog.component';
import { Resource } from 'src/models/Resource';
import { JobService } from 'src/services/jobService';

@Component({
  selector: 'app-stocks',
  templateUrl: './stocks.component.html',
  styleUrls: ['./stocks.component.css']
})
export class StocksComponent implements OnInit {
  displayedColumns: string[] = [ 'id','name','type','quantity','createdAt', 'actions'];
  resourcesDataSource: MatTableDataSource<Resource> = new MatTableDataSource<Resource>();
  paginatedData: MatTableDataSource<Resource> = new MatTableDataSource<Resource>();
  @ViewChild(MatPaginator) paginator: any;
  resourceList: Resource[] = [];

  constructor(private jobService : JobService,
    private dialog : MatDialog,
    private _snackBar : MatSnackBar
    ) { }

  ngOnInit(): void {
    this.getDataSource()
  }

   getDataSource() {
    this.resourcesDataSource = new MatTableDataSource<Resource>();
    this.paginatedData = new MatTableDataSource<Resource>();
    this.jobService.getResources().subscribe({
      next: (response: Resource[]) => {
        this.resourcesDataSource.data.push(...response);
        this.paginatedData = new MatTableDataSource<Resource>( this.resourcesDataSource.data.slice(0, 10))
        this.resourceList = response
      },
      error: (error: any) => console.log('error during getAllResources:', error),
      complete :  () => {
        this.resourcesDataSource.paginator = this.paginator; 
      }
    });
  }

  handlePageEvent(event: PageEvent): void {
    const startIndex = event.pageIndex * event.pageSize;
    const endIndex = startIndex + event.pageSize;
    const paginatedData = this.resourcesDataSource.data.slice(startIndex, endIndex);
    this.paginatedData = new MatTableDataSource<Resource>(paginatedData);
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
    // Custom filtering function
    this.resourcesDataSource.filterPredicate = (resource: Resource, filter: string) => {
      const id = resource.id.toString()
      const name = resource.name.toLowerCase();
      const type = resource.type.toLowerCase();
      const quantity = resource.quantity.toString().toLowerCase();      
      const createdAt = resource.createdAt.toLowerCase(); 
      return (
        id.includes(filter) ||
        name.includes(filter) ||
        type.includes(filter) ||
        quantity.includes(filter) ||
        createdAt.includes(filter)
      );
    };
    // Apply the filter
    this.resourcesDataSource.filter = filterValue;
    // Reset the paginator to the first page
    if (this.paginator) {
      this.paginator.firstPage();
    }
    // Update the paginatedData with the filtered data
    const startIndex = this.paginator?.pageIndex * this.paginator?.pageSize || 0;
    const endIndex = startIndex + (this.paginator?.pageSize || 10);
    const paginatedData = this.resourcesDataSource.filteredData.slice(startIndex, endIndex);
    this.paginatedData = new MatTableDataSource<Resource>(paginatedData);
  }

  deleteResource(resource : any){
    let dialogRef = this.dialog.open(ConfirmDialogComponent, {

      width: '400px',
      data : { title : "Delete Resource ?", content : `Are you sure you want to delete  resource ${resource.name} ? This will delete all related orders who need this resource.`}
    })

    dialogRef.afterClosed().subscribe((result : string) =>{
      if(result=="yes"){

        this.jobService.deleteResource(resource.id).subscribe({
          next : (response : any)  => {  
            this.openSnackBar(" Resource Deleted.")
            this.getDataSource()
          },
          error : (error: any) => { console.error('Error deleting resource', error); } 
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
