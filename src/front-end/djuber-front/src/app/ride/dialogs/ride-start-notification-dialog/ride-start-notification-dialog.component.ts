import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { RideSocketResponse } from '../../rideSocketResponse';
@Component({
  selector: 'djuber-ride-start-notification-dialog',
  templateUrl: './ride-start-notification-dialog.component.html',
  styleUrls: ['./ride-start-notification-dialog.component.css'],
})
export class RideStartNotificationDialogComponent implements OnInit {
  dataToDisplay: RideSocketResponse;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: RideSocketResponse,
    public dialogRef: MatDialogRef<RideStartNotificationDialogComponent>,
    private router: Router
  ) {
    this.dataToDisplay = data['response'];
    console.log(this.dataToDisplay);
  }

  ngOnInit(): void {}

  showOnMap(): void {
    this.dialogRef.close();
    this.router.navigate(['singleRideMap', this.dataToDisplay.rideId]);
  }
}
