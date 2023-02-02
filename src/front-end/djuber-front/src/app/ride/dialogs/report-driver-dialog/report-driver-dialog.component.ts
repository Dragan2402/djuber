import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { SnackbarComponent } from 'src/app/snackbar/snackbar.component';
import { RideService } from '../../ride.service';

@Component({
  selector: 'djuber-report-driver-dialog',
  templateUrl: './report-driver-dialog.component.html',
  styleUrls: ['./report-driver-dialog.component.css']
})
export class ReportDriverDialogComponent implements OnInit {

  rideId:string;

  reason:string;

  constructor(public dialogRef: MatDialogRef<ReportDriverDialogComponent>, private snackBar:MatSnackBar, @Inject(MAT_DIALOG_DATA) public data: any, private router: Router, private rideService:RideService) {
    this.rideId = data["data"];
   }

  ngOnInit(): void {
  }

  submit(){
    if(this.reason.length<5){
      this.snackBar.openFromComponent(SnackbarComponent,{data:"Please provide a report reason."});
      return;
    }
    this.rideService.submitDriverReport(this.rideId,this.reason).subscribe({
      complete:()=>{
        this.snackBar.openFromComponent(SnackbarComponent,{data:"Report successfully saved."});
        this.dialogRef.close();
        this.router.navigate(["/"]);
      }
    })
  }

}
