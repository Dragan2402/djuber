import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { RideSocketResponse } from '../../rideSocketResponse';

@Component({
  selector: 'djuber-accept-ride-client-dialog',
  templateUrl: './accept-ride-client-dialog.component.html',
  styleUrls: ['./accept-ride-client-dialog.component.css']
})
export class AcceptRideClientDialogComponent implements OnInit {

  dataToDisplay:RideSocketResponse;

  constructor(public dialogRef: MatDialogRef<AcceptRideClientDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: RideSocketResponse, private router: Router) {
    this.dataToDisplay = data;
   }

  ngOnInit(): void {
  }

}
