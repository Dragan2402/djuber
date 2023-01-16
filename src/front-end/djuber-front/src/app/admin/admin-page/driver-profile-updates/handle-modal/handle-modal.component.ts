import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogConfig, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AdminService } from 'src/app/admin/admin.service';
import { DriverUpdate } from '../driverUpdate';

@Component({
  selector: 'djuber-handle-modal',
  templateUrl: './handle-modal.component.html',
  styleUrls: ['./handle-modal.component.css']
})
export class HandleModalComponent implements OnInit {

  driver:DriverUpdate = undefined;

  constructor(public dialogRef: MatDialogRef<HandleModalComponent>, @Inject(MAT_DIALOG_DATA) public data: DriverUpdate, private adminSerivce : AdminService) {
    this.driver = data["driver"];
  }

  ngOnInit(): void {
  }

  accept(){
    this.adminSerivce.acceptDriverDataChange(this.driver.requestId).subscribe({complete:()=>{
      this.dialogRef.close()
    }});
  }

  decline(){
    this.adminSerivce.declineDriverDataChange(this.driver.requestId).subscribe({complete:()=>{
      this.dialogRef.close()
    }});
  }

}
