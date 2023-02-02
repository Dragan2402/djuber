import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatIconModule} from '@angular/material/icon';
import { HomePageComponent } from './home-page/home-page.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import {GlobalErroHandler} from "./utility/errorhandler.service"
import {RequestInterceptor} from "./utility/request.interceptor";
import { HeaderBarComponent } from './header-bar/header-bar.component';
import {MatMenuModule} from '@angular/material/menu';
import {MatSnackBarModule, MAT_SNACK_BAR_DEFAULT_OPTIONS} from '@angular/material/snack-bar';
import { FacebookLoginProvider, GoogleLoginProvider, SocialAuthServiceConfig } from '@abacritt/angularx-social-login';
import { SnackbarComponent } from './snackbar/snackbar.component';
import { LiveChatComponent } from './live-chat/live-chat.component';
import { UserLiveChatComponent } from './home-page/user-live-chat/user-live-chat.component';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatDividerModule} from '@angular/material/divider';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { MapComponent } from './home-page/map/map.component';
import {MatRadioModule} from '@angular/material/radio';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatSelectModule} from '@angular/material/select';
import { AcceptRideDriverDialogComponent } from './ride/dialogs/accept-ride-driver-dialog/accept-ride-driver-dialog.component';
import {MatDialogModule} from '@angular/material/dialog';
import { SingleRideMapComponent } from './ride/single-ride-map/single-ride-map.component';
import { AcceptRideClientDialogComponent } from './ride/dialogs/accept-ride-client-dialog/accept-ride-client-dialog.component';
import { RideReviewComponent } from './ride/ride-review/ride-review.component';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import { CancelRideNoteDialogComponent } from './ride/dialogs/cancel-ride-note-dialog/cancel-ride-note-dialog.component';
import { MatStepperModule } from '@angular/material/stepper';
import { RideStartNotificationDialogComponent } from './ride/dialogs/ride-start-notification-dialog/ride-start-notification-dialog.component';
import { ReportDriverDialogComponent } from './ride/dialogs/report-driver-dialog/report-driver-dialog.component';


@NgModule({
  declarations: [
    AppComponent,
    HomePageComponent,
    NotFoundComponent,
    HeaderBarComponent,
    SnackbarComponent,
    LiveChatComponent,
    UserLiveChatComponent,
    MapComponent,
    AcceptRideDriverDialogComponent,
    SingleRideMapComponent,
    AcceptRideClientDialogComponent,
    RideReviewComponent,
    CancelRideNoteDialogComponent,
    RideStartNotificationDialogComponent,
    ReportDriverDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatIconModule,
    HttpClientModule,
    MatMenuModule,
    MatSnackBarModule,
    MatButtonModule,
    MatCardModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatDividerModule,
    MatInputModule,
    LeafletModule,
    MatRadioModule,
    MatCheckboxModule,
    MatTooltipModule,
    MatProgressBarModule,
    MatSelectModule,
    MatDialogModule,
    MatButtonToggleModule
  ],
  providers: [
    { provide: ErrorHandler, useClass: GlobalErroHandler },
    { provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: {horizontalPosition: 'center',
      verticalPosition: 'top', panelClass : "snackbar", duration:1500}},
    { provide: HTTP_INTERCEPTORS, useClass: RequestInterceptor, multi: true},
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(
              '100277748544-laavrf4esjhhhqa4i0kt8lkqlupbgpnj.apps.googleusercontent.com'
            )
          },
          {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider('2743619645775662')
          }
        ],
        onError: (err) => {
          console.error(err);
        }
      } as SocialAuthServiceConfig,
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
