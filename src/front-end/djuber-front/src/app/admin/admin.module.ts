import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { AdminPageComponent } from './admin-page/admin-page.component';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatListModule} from '@angular/material/list';
import {MatDividerModule} from '@angular/material/divider';
import {MatMenuModule} from '@angular/material/menu';
import { AdminProfileComponent } from './admin-page/admin-profile/admin-profile.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { AdminService } from './admin.service';
import { RegisterDriverComponent } from './admin-page/register-driver/register-driver.component';
import {MatStepperModule} from '@angular/material/stepper';
import {MatCheckboxModule} from '@angular/material/checkbox';
import { DriversPreviewComponent } from './admin-page/drivers-preview/drivers-preview.component';
import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';
import { ClientPreviewComponent } from './admin-page/client-preview/client-preview.component';

@NgModule({
  declarations: [
    AdminPageComponent,
    AdminProfileComponent,
    RegisterDriverComponent,
    DriversPreviewComponent,
    ClientPreviewComponent
  ],
  providers: [AdminService],
  imports: [
    CommonModule,
    AdminRoutingModule,
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatListModule,
    MatButtonModule,
    MatDividerModule,
    MatMenuModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatInputModule,
    MatStepperModule,
    MatCheckboxModule,
    MatTableModule,
    MatPaginatorModule
  ]
})
export class AdminModule { }
