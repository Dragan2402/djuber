import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackbarComponent } from 'src/app/snackbar/snackbar.component';
import { ClientService } from '../../client.service';

declare let paypal: any;

@Component({
  selector: 'djuber-client-payment',
  templateUrl: './client-payment.component.html',
  styleUrls: ['./client-payment.component.css']
})
export class ClientPaymentComponent implements OnInit {

  amount:number = 5;

  today = new Date();

  @ViewChild('paypal', { static: true }) paypalElement: ElementRef;


  constructor(private clientService:ClientService, private _snackBar: MatSnackBar) { }

  ngOnInit() {
    paypal
      .Buttons({

        createOrder: (data: any, actions: any) => {
          return actions.order.create({
            purchase_units: [
              {
                description: `Djuber drives purchase`,
                amount: {
                  currency_code: 'USD',
                  value: this.amount
                },
                payee:{email_address : "sb-ukbxu24922700@business.example.com"}
              }
            ]
          })
        },
        onApprove: (data: any, actions: any) => {
          return actions.order.capture().then( () => {
            this.clientService.addLoggedClientFunds(this.amount*108).subscribe();
            this._snackBar.openFromComponent(SnackbarComponent,{data:"Your payment has successfully done."})
          })
        },
        onError: err => {
          console.log(err);
          console.log("Error")
          }
      })
      .render(this.paypalElement.nativeElement);

  }


}
