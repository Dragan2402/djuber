import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DriverRoutingModule } from './driver-routing.module';
import { DriverPageComponent } from './driver-page/driver-page.component';
import { DriverProfileComponent } from './driver-page/driver-profile/driver-profile.component';
import { DriverService } from './driver.service';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatListModule} from '@angular/material/list';
import {MatDividerModule} from '@angular/material/divider';
import {MatMenuModule} from '@angular/material/menu';

@NgModule({
  declarations: [
    DriverPageComponent,
    DriverProfileComponent
  ],
  providers: [DriverService],
  imports: [
    CommonModule,
    DriverRoutingModule,
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
    ReactiveFormsModule
  ]
})
export class DriverModule { }
