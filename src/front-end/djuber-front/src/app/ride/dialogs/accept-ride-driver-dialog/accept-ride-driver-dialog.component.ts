import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { RideSocketResponse } from '../../rideSocketResponse';

@Component({
  selector: 'djuber-accept-ride-driver-dialog',
  templateUrl: './accept-ride-driver-dialog.component.html',
  styleUrls: ['./accept-ride-driver-dialog.component.css']
})
export class AcceptRideDriverDialogComponent implements OnInit {

  dataToDisplay:RideSocketResponse;

  constructor(public dialogRef: MatDialogRef<AcceptRideDriverDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: RideSocketResponse, private router: Router) {
    this.dataToDisplay = data["response"];
    console.log(data);
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
    this.router.navigate(["singleRideMap",this.dataToDisplay.rideId]);
  }
}
