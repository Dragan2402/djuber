import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { SnackbarComponent } from 'src/app/snackbar/snackbar.component';
import { RideService } from '../ride.service';
import { RideReviewResponse } from './rideReviewResponse';

@Component({
  selector: 'djuber-ride-review',
  templateUrl: './ride-review.component.html',
  styleUrls: ['./ride-review.component.css']
})
export class RideReviewComponent implements OnInit {

  rideId:string;
  rideReviewResponse:RideReviewResponse;
  carRating:number = 0;
  driverRating:number = 0;
  comment:string = "";

  constructor(private route: ActivatedRoute, private rideService:RideService, private snackBar : MatSnackBar, private router:Router) {
   }


  ngOnInit(): void {
    this.rideId = this.route.snapshot.paramMap.get('id');
    this.rideService.getRideForReview(this.rideId).subscribe({
      next:(response) =>{
        this.rideReviewResponse = response;
      },
      error:(err) =>{
        this.snackBar.openFromComponent(SnackbarComponent, {data:"Review for this ride is not available."});
      }
    })
  }

  submitReview(){
    if(this.carRating===0){
      this.snackBar.openFromComponent(SnackbarComponent, {data:"Please rate the car."});
      return;
    }if(this.driverRating === 0){
      this.snackBar.openFromComponent(SnackbarComponent, {data:"Please rate the driver."});
      return;
    }
    const request = {carRating:this.carRating, driverRating:this.driverRating, comment:this.comment, rideId:this.rideId};

    this.rideService.reviewRide(request).subscribe({
      complete:()=>{
        this.snackBar.openFromComponent(SnackbarComponent, {data:"Your review has been added."});
        this.router.navigate(["/"]);
      },
      error:(err)=>{
        this.snackBar.openFromComponent(SnackbarComponent, {data:"Sorry we can not accept your."});
      }
    });
  }

}
