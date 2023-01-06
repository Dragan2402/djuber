import { Component, Inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA } from '@angular/material/snack-bar';

@Component({
  selector: 'djuber-snackbar',
  templateUrl: './snackbar.component.html',
  styleUrls: ['./snackbar.component.css']
})
export class SnackbarComponent {

  message:string;

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data: string) {
    this.message = data;
   }

}
