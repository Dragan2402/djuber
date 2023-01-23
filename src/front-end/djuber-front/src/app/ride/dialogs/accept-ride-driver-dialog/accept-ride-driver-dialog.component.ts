import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'djuber-accept-ride-driver-dialog',
  templateUrl: './accept-ride-driver-dialog.component.html',
  styleUrls: ['./accept-ride-driver-dialog.component.css']
})
export class AcceptRideDriverDialogComponent implements OnInit {

  dataToDisplay:any;

  constructor(public dialogRef: MatDialogRef<AcceptRideDriverDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any, private router: Router) {
    this.dataToDisplay = data;
  }

  ngOnInit(): void {
  }

  declineRide(){
    console.log("Declining");
    this.dialogRef.close();
  }

  acceptRide(){
    console.log("Accepting");
    this.dialogRef.close();
    this.router.navigate(["singleRideMap",1232131]);
  }
}
