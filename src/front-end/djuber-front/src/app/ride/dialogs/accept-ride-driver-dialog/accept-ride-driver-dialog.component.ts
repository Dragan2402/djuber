import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { RideService } from '../../ride.service';
import { RideSocketResponse } from '../../rideSocketResponse';

@Component({
  selector: 'djuber-accept-ride-driver-dialog',
  templateUrl: './accept-ride-driver-dialog.component.html',
  styleUrls: ['./accept-ride-driver-dialog.component.css']
})
export class AcceptRideDriverDialogComponent implements OnInit {

  dataToDisplay:RideSocketResponse;

  constructor(public dialogRef: MatDialogRef<AcceptRideDriverDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: RideSocketResponse, private router: Router, private rideService:RideService) {
    this.dataToDisplay = data["response"];
    console.log(data);
  }

  ngOnInit(): void {
  }

  declineRide(){
    this.rideService.declineRideDriver(this.dataToDisplay.rideId).subscribe();
    this.dialogRef.close();
  }

  acceptRide(){
    this.rideService.acceptRideDriver(this.dataToDisplay.rideId).subscribe();
    this.dialogRef.close();
    // this.router.navigate(["singleRideMap",this.dataToDisplay.rideId]);
  }
}
