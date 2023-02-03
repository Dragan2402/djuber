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
import { NoteModalComponent } from './admin-page/note-modal/note-modal.component';
import {MatDialogModule} from '@angular/material/dialog';
import { AdminLiveChatComponent } from './admin-page/admin-live-chat/admin-live-chat.component';
import {MatCardModule} from '@angular/material/card';
import {MatBadgeModule} from '@angular/material/badge';
import { DriverProfileUpdatesComponent } from './admin-page/driver-profile-updates/driver-profile-updates.component';
import { HandleModalComponent } from './admin-page/driver-profile-updates/handle-modal/handle-modal.component';
import {ClientRidesComponent} from "./admin-page/client-preview/client-rides/client-rides.component";
import {ClientService} from "../client/client.service";
import {DriverService} from "../driver/driver.service";
import {DriverRidesComponent} from "./admin-page/drivers-preview/driver-rides/driver-rides.component";


@NgModule({
    declarations: [
        AdminPageComponent,
        AdminProfileComponent,
        RegisterDriverComponent,
        DriversPreviewComponent,
        ClientPreviewComponent,
        NoteModalComponent,
        AdminLiveChatComponent,
        DriverProfileUpdatesComponent,
        HandleModalComponent,
        ClientRidesComponent,
        DriverRidesComponent
    ],
  providers: [AdminService, ClientService, DriverService],
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
    MatPaginatorModule,
    MatDialogModule,
    MatCardModule,
    MatBadgeModule
  ]
})
export class AdminModule { }
