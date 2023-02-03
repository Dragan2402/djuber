import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {ClientRoutingModule} from './client-routing.module';
import {ClientPageComponent} from './client-page/client-page.component';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatListModule} from '@angular/material/list';
import {MatDividerModule} from '@angular/material/divider';
import {MatMenuModule} from '@angular/material/menu';
import {ClientProfileComponent} from './client-page/client-profile/client-profile.component';
import {ClientService} from './client.service';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ClientPaymentComponent} from './client-page/client-payment/client-payment.component';
import {MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import {ClientRidesComponent} from "./client-page/client-rides/client-rides.component";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatTableModule} from "@angular/material/table";
import {
  ClientFavouriteRidesDialogComponent
} from '../home-page/map/client-favourite-rides-dialog/client-favourite-rides-dialog.component';
import {ModalComponent} from "../components/modal/modal.component";
import {NgxChartsModule} from "@swimlane/ngx-charts";
import {ClientReportsComponent} from "./client-page/client-report/client-reports.component";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {NgbDatepickerModule} from "@ng-bootstrap/ng-bootstrap";
import {NgbdDatepickerRange} from "../components/datepicker/datepicker-range";

@NgModule({
  declarations: [
    ClientPageComponent,
    ClientProfileComponent,
    ClientPaymentComponent,
    ClientRidesComponent,
    ClientReportsComponent,
    ClientFavouriteRidesDialogComponent,
    ModalComponent
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
    MatPaginatorModule,
    MatTableModule,
    NgxChartsModule,
    MatDatepickerModule,
    NgbDatepickerModule,
    NgbdDatepickerRange
  ]
})
export class ClientModule { }
