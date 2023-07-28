  import { Component, AfterViewInit, ViewChildren, QueryList, ElementRef, OnInit, ViewChild } from '@angular/core';
  import { Chart, registerables } from 'chart.js';
  import { Planning } from 'src/models/Planning';
  import { JobService } from 'src/services/jobService';
  import * as moment from 'moment';
  import { trigger, state, style, animate, transition } from '@angular/animations';
  import { Phase } from 'src/models/Phase';
  import { Job } from 'src/models/Job';
  import { Task } from 'src/models/Task';
import { ActivatedRoute } from '@angular/router';
  

  @Component({
    selector: 'app-schedule',
    templateUrl: './schedule.component.html',
    styleUrls: ['./schedule.component.css'],
    animations: [
      trigger('expandCollapse', [
        state(
          'collapsed',
          style({ height: '0px', opacity: 0, overflow: 'hidden' })
        ),
        state('expanded', style({ height: '*', opacity: 1 })),
        transition('collapsed <=> expanded', animate('250ms ease-in-out'))
      ])
    ]
  })
  export class ScheduleComponent implements OnInit, AfterViewInit {

    @ViewChild('lottieAnimation') lottieAnimation!: ElementRef;
    @ViewChildren('chartCanvas') chartCanvases!: QueryList<ElementRef>;
    expandedIndex: number = -1;
    plannings: Planning[] = [];
    filteredPlannings: Planning[] = [];

    private charts: Chart[] = [];
    lottieAnimations: boolean[] = [];
    phases: Phase[] = [];
    
    selectedDate : Date = new  Date();
    loading = true;
    generating = false;
    constructor(private jobService: JobService, private activatedRoute : ActivatedRoute) { 

      if(this.activatedRoute.snapshot.url.join("/").endsWith("new")){
        this.generateNewPlanning()
      }
      
    }

      ngOnInit() {
      this.loading = true
      Chart.register(...registerables);
      this.getAllphases(); 
      this.loadPlannings();
      
    }

    startLottieAnimation(index: number) {
      this.lottieAnimations[index] = true;
    }

    stopLottieAnimation(index: number) {
      this.lottieAnimations[index] = false;
    }

    ngAfterViewInit() {
      setTimeout(() => {

      console.log("filteredPlannings :",this.filteredPlannings);
      console.log("Plannings :",this.plannings);
      this.initializeCharts();
    }, 300);  
    }


    loadPlannings() {
      this.loading = true
      this.jobService.getAllPlannings().subscribe({
        next : (plannings: Planning[]) => {
          this.plannings = plannings
          
    },
    complete : () => {
         this.filterPlannings()
         this.generating = false;
        } 
  });
}


   filterPlannings() {   
    this.loading = true
    this.filteredPlannings = this.plannings.filter(planning =>
      moment(planning.createdAt).isSame(this.selectedDate, 'day')
    );

    setTimeout(() => {
      console.log("filteredPlannings :",this.filteredPlannings);
      this.initializeCharts();
    }, 500);  
    this.loading = false  
  }

    

    async initializeCharts() {
     this.chartCanvases.forEach((canvas, index) => {
        const ctx = canvas.nativeElement.getContext('2d');
        if (!ctx) return;

        const chart = new Chart(ctx, {
          type: 'scatter',
          data: {
            datasets: []
          },
          options: {
            responsive: true,
            scales: {
              x: {
                type: 'linear',
                ticks: {
                  callback: (value) => {
                    const date = new Date(value);
                    return moment(date, 'MMM D, YYYY HH:mm').format('MMM D, YYYY HH:mm');
                  }
                },
                title: {
                  display: true,
                  text: 'Date'
                }
              },
              y: {
                title: {
                  display: true,
                  text: 'Num Order'
                }
              }
            },
            plugins: {
              tooltip: {
                callbacks: {
                  label: (context) => {
                    const dataPoint = context.parsed.x;
                    return moment(dataPoint).format('MMM D, YYYY HH:mm');
                  }
                }
              }
            }
          }
        });

        this.charts.push(chart);

        const planning = this.filteredPlannings[index];
        const data = planning.jobList.map(job => ({
          x: job.startDateTime? new Date(job.startDateTime).getTime() :  new Date().getTime(),
          y: job.numOrder,
          r: 2,
          backgroundColor: this.getRandomColor() // Assign random color to each data point
        }));

        chart.data.datasets.push({
          label: 'Orders',
          data: data,
        });

        chart.update();
      });

    }

    getRandomColor() {
      const letters = '0123456789ABCDEF';
      let color = '#';
      for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
      }
      return color;
    }

    getIconName(rate: number): string {
      if (rate > 90) {
        return 'good-rate.json';
      } else if (rate > 70) {
        return 'average-rate.json';
      } else {
        return 'bad-rate.json';
      }
    }

    getRateText(rate: number): string {
      if (rate > 95) {
        return ' Very Good';
      } else if (rate > 75) {
        return ' Pretty Good';
      } else if( rate > 50 ) {
        return ' Average';
      }else{
        return ' Bad';
      }
    }


    getAllphases(): void {
      this.jobService.getAllPhases().subscribe({
        next: (response) => {
          this.phases = response
          }
      })
      
    }

    getTaskList(jobList : Job[]): Task[]{
      let tasks : Task[] = []
      jobList.forEach((job) => job.taskList.forEach((task) => tasks.push(task)))
      return tasks
      }

      async generateNewPlanning(){
        this.generating = true;
        this.jobService.makeNewPlanning().subscribe({
          next : (response) => {
            this.plannings.push(response)
             },
          error : (error) => { console.log(error)},
          complete : () => { this.loadPlannings()}
        })
      }
  }


