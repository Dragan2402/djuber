import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { RideSocketResponse } from '../../rideSocketResponse';
import { RideService } from '../../ride.service';

@Component({
  selector: 'djuber-accept-ride-client-dialog',
  templateUrl: './accept-ride-client-dialog.component.html',
  styleUrls: ['./accept-ride-client-dialog.component.css'],
})
export class AcceptRideClientDialogComponent implements OnInit {
  dataToDisplay: RideSocketResponse;
  balance: number;

  constructor(
    public dialogRef: MatDialogRef<AcceptRideClientDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: RideSocketResponse,
    private router: Router,
    private rideService: RideService
  ) {
    this.dataToDisplay = data['response'];
    console.log(data);
  }

  ngOnInit(): void {
    this.rideService.getLoggedClientBalance().subscribe({
      next: (response) => {
        this.balance = response['balance'];
      },
    });
  }

  declineRide() {
    this.rideService.declineRideClient(this.dataToDisplay.rideId).subscribe();
    this.dialogRef.close();
  }

  acceptRide() {
    console.log(this.dataToDisplay.rideId);
    this.rideService.acceptRideClient(this.dataToDisplay.rideId).subscribe();
    this.dialogRef.close();
    // this.router.navigate(['singleRideMap', this.dataToDisplay.rideId]);
  }
}
