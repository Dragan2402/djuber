import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { SnackbarComponent } from 'src/app/snackbar/snackbar.component';
import { RideService } from '../../ride.service';

@Component({
  selector: 'djuber-cancel-ride-note-dialog',
  templateUrl: './cancel-ride-note-dialog.component.html',
  styleUrls: ['./cancel-ride-note-dialog.component.css']
})
export class CancelRideNoteDialogComponent implements OnInit {

  rideId:string;

  note:string;

  constructor(public dialogRef: MatDialogRef<CancelRideNoteDialogComponent>, private snackBar:MatSnackBar, @Inject(MAT_DIALOG_DATA) public data: any, private router: Router, private rideService:RideService) {
    this.rideId = data["data"];
   }

  ngOnInit(): void {
  }


  submit(){
    if(this.note.length<5){
      this.snackBar.openFromComponent(SnackbarComponent,{data:"Please provide a reason for declining ride."});
      return;
    }
    this.rideService.submitCancellingNote(this.rideId,this.note).subscribe({
      complete:()=>{
        this.snackBar.openFromComponent(SnackbarComponent,{data:"Note successfully saved."});
        this.dialogRef.close();
        this.router.navigate(["/"]);
      }
    })
  }
}
