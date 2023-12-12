import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { FileHandle } from 'src/models/FileHandle';
import { JobService } from 'src/services/jobService';

@Component({
  selector: 'app-simulator',
  templateUrl: './simulator.component.html',
  styleUrls: ['./simulator.component.css']
})
export class SimulatorComponent implements OnInit {

  showExcelImage : boolean = false
  uploadForm !: FormGroup 
  plannedFile : any
  loading : boolean = true;
  uploadFile: File | null = null 
  error : boolean = false
  
  constructor(private _formBuilder: FormBuilder,
    private sanitizer : DomSanitizer,
    private jobService : JobService) {
    this.uploadForm = _formBuilder.group({
      file : ['', Validators.required]
    })
   }

  ngOnInit(): void {
  }

  onFileSelected(event: any) {
    this.uploadFile = event.target.files[0];
  
    if (this.uploadFile ) {

      const fileHandle : FileHandle = {
        file : this.uploadFile,
        url : this.sanitizer.bypassSecurityTrustUrl(window.URL.createObjectURL(this.uploadFile))
      }
      
    }
  }

  sendUploadedFile(){
    this.loading = true

    if(this.uploadFile){
      console.log(this.uploadFile)
      const formData: FormData = new FormData();
      formData.append('file',this.uploadFile, this.uploadFile.name);
      this.jobService.simulateWithExcelFile(formData).subscribe({
        next : (response : File) => {
          this.plannedFile = response
          this.loading = false;
          console.log("Scheduled file is ready.")
        },

        error : (error : any) => { 
          console.log("error during solving by file : ",error);
          this.error = true
      }
      })
    }else{
      console.log("No file sended")
    }

  }

  downloadFile(){

    const currentDate = new Date();
    const formattedDate = currentDate.toISOString().split('T')[0];
    const filename = `PlannedFile-${formattedDate}.xlsx`;

    const anchorElement = document.createElement('a');
    anchorElement.href = window.URL.createObjectURL(this.plannedFile);
    anchorElement.download = filename;
    document.body.appendChild(anchorElement);
    anchorElement.click();
  }
}
