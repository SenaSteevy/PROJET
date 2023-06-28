import { AfterViewInit, Component, Input, OnInit} from '@angular/core';
import { Job } from 'src/models/Job';
import { MatTableDataSource } from '@angular/material/table';
import { Task } from 'src/models/Task';
import { ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements  AfterViewInit {
  @Input() jobList: Job[] = [];
  @Input() phase: any
  loading: boolean = true;
  displayedColumns: string[] = ['numOrder', 'type', 'startTime', 'status', 'actions'];

  @ViewChild("paginator", { static: true }) paginator!: MatPaginator;


  taskListDataSource: MatTableDataSource<Task> = new MatTableDataSource<Task>();
  paginatedData: MatTableDataSource<Task> = new MatTableDataSource<Task>();

 
  constructor() {}

  handlePageEvent(event: PageEvent): void {
    const startIndex = event.pageIndex * event.pageSize;
    const endIndex = startIndex + event.pageSize;
    const paginatedData = this.taskListDataSource.data.slice(startIndex, endIndex);
    this.paginatedData = new MatTableDataSource<Task>(paginatedData);
  }

  ngOnInit(): void {
    this.loading = true;
    this.findPhasesAndDataSource();
    this.paginatedData = new MatTableDataSource<Task>( this.taskListDataSource.data.slice(0, 9))
  }

  ngAfterViewInit() {

    
  }

  findPhasesAndDataSource(): void {
      
        this.jobList.forEach((job) => {
            const tasksForPhase = job.taskList.filter((task) => task.phase.name === this.phase.name);
            this.taskListDataSource.data.push(...tasksForPhase);
          });

          setTimeout(() => {
            this.loading = false;
          }, 7000);
      
      }
  


}
