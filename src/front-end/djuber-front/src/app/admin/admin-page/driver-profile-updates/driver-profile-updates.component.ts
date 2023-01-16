import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AdminService } from '../../admin.service';
import { DriverUpdate } from './driverUpdate';
import { HandleModalComponent } from './handle-modal/handle-modal.component';

@Component({
  selector: 'djuber-driver-profile-updates',
  templateUrl: './driver-profile-updates.component.html',
  styleUrls: ['./driver-profile-updates.component.css']
})
export class DriverProfileUpdatesComponent implements OnInit {

  displayedColumns: string[] = ['id', 'firstName', 'lastName', 'email', 'city', 'carType' , 'licensePlate','handle'];

  drivers:DriverUpdate[];

  constructor(private adminService : AdminService, public matDialog: MatDialog) { }

  ngOnInit(): void {
    this.adminService.getDriverProfileUpdates().subscribe({
      next: (response) =>{
        console.log(response);
        this.drivers = response;
      }
    });
  }


  toggleHandle(driver:DriverUpdate){
    const dialogConfig = new MatDialogConfig();
    // The user can't close the dialog by clicking outside its body
    dialogConfig.disableClose = false;
    dialogConfig.id = "handle-modal";
    dialogConfig.data = {driver};
    dialogConfig.height = "80%";
    dialogConfig.width = "30%";
    // https://material.angular.io/components/dialog/overview
    const dialogRef = this.matDialog.open(HandleModalComponent, dialogConfig);
    dialogRef.afterClosed().subscribe({
      complete:()=>{
        this.adminService.getDriverProfileUpdates().subscribe({
          next: (response) =>{
            console.log(response);
            this.drivers = response;
          }
        });
      }
    })
  }

}
