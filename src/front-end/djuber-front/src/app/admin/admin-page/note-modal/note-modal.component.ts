import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AdminService } from '../../admin.service';

@Component({
  selector: 'djuber-note-modal',
  templateUrl: './note-modal.component.html',
  styleUrls: ['./note-modal.component.css']
})
export class NoteModalComponent implements OnInit {

  note:string;
  isClient:boolean = false;
  id:number;

  constructor(public dialogRef: MatDialogRef<NoteModalComponent>, @Inject(MAT_DIALOG_DATA) public data: number, private adminSerivce : AdminService) {
    this.isClient = data["isClient"];
    this.id = data["id"];
    if(this.isClient){
      adminSerivce.getClientNote(this.id).subscribe({next : (response) =>{
        this.note = response["note"];
      }});
    }else{
      adminSerivce.getDriverNote(this.id).subscribe({next : (response) =>{
        this.note = response["note"];
      } });
    }
  }

  editing:boolean = false;

  ngOnInit(): void {

  }

  closeDialog(){
    this.dialogRef.close();
  }

  edit(){
    this.editing = true;
  }

  save(){
    this.editing = false;
    if(this.isClient){
      this.adminSerivce.updateClientNote(this.id, this.note).subscribe().unsubscribe();
    }else{
      this.adminSerivce.updateDriverNote(this.id, this.note).subscribe().unsubscribe();
    }
  }

}
