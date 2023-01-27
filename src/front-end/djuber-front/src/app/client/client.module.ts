import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClientRoutingModule } from './client-routing.module';
import { ClientPageComponent } from './client-page/client-page.component';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatListModule} from '@angular/material/list';
import {MatDividerModule} from '@angular/material/divider';
import {MatMenuModule} from '@angular/material/menu';
import { ClientProfileComponent } from './client-page/client-profile/client-profile.component';
import { ClientService } from './client.service';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { ClientPaymentComponent } from './client-page/client-payment/client-payment.component';
import {MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import { ClientFavouriteRidesDialogComponent } from '../home-page/map/client-favourite-rides-dialog/client-favourite-rides-dialog.component';
import {MatTableModule} from '@angular/material/table';

@NgModule({
  declarations: [
    ClientPageComponent,
    ClientProfileComponent,
    ClientPaymentComponent,
    ClientFavouriteRidesDialogComponent
  ],
  providers:[ClientService ,{
    provide: MatDialogRef,
    useValue: {}
  },],
  imports: [
    CommonModule,
    ClientRoutingModule,
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatListModule,
    MatDividerModule,
    MatMenuModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatTableModule
  ]
})
export class ClientModule { }
