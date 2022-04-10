import { HttpClientModule, HttpClient } from '@angular/common/http';
import { ErrorHandler, NgModule } from '@angular/core';
import { BrowserModule, HAMMER_GESTURE_CONFIG, HammerGestureConfig, HammerModule } from '@angular/platform-browser';

import { AppRoutingModule, RoutingComponents } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './component/home/home.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';
import { NavBarComponent } from './component/nav-bar/nav-bar.component';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner'
import { MatIconModule } from '@angular/material/icon'
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BetPageComponent } from './page/bet-page/bet-page.component';
import { BetComponent } from './component/bet/bet.component';
import { BetButtonComponent } from './component/bet-button/bet-button.component';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatToolbarModule } from '@angular/material/toolbar'
import { MatCardModule } from '@angular/material/card'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input'
import { MatSelectModule } from '@angular/material/select'
import { GlobalErrorHandler } from './global-error-handler';
import { BetItemDialogComponent } from './component/dialog/bet-item-dialog/bet-item-dialog.component';
import { LoginComponent } from './component/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AccessDeniedDialogComponent } from './component/dialog/access-denied-dialog/access-denied-dialog.component';
import { LeagueDialogComponent } from './component/dialog/league-dialog/league-dialog.component';
import { BetResultsComponent } from './component/bet-results/bet-results.component';
import { LeagueOverviewComponent } from './component/league-overview/league-overview.component';
import { BetSubjectComponent } from './component/bet-subject/bet-subject.component';
import { AdminControlButtonComponent } from './component/button/admin-control-button/admin-control-button.component';
import { RaceToBetRedirectComponent } from './component/race-to-bet-redirect/race-to-bet-redirect.component';
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import { UserDialogComponent } from './component/dialog/user-dialog/user-dialog.component';
import { DragDropModule } from '@angular/cdk/drag-drop';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [
    AppComponent,
    RoutingComponents,
    NavBarComponent,
    BetPageComponent,
    BetComponent,
    BetButtonComponent,
    BetItemDialogComponent,
    BetSubjectComponent,
    LoginComponent,
    AccessDeniedDialogComponent,
    LeagueDialogComponent,
    BetResultsComponent,
    LeagueOverviewComponent,
    AdminControlButtonComponent,
    RaceToBetRedirectComponent,
    UserDialogComponent,
  ],
  imports: [
    BrowserModule,
    HammerModule,
    HttpClientModule,
    AppRoutingModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatSnackBarModule,
    MatToolbarModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    FormsModule,
    ReactiveFormsModule,
    DragDropModule,
    ServiceWorkerModule.register('custom-service-worker.js', {
      enabled: environment.production,
      // Register the ServiceWorker as soon as the app is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000'
    }),
    BrowserAnimationsModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      },
      defaultLanguage: 'en',
    })
  ],
  providers: [{
    provide: ErrorHandler,
    useClass: GlobalErrorHandler
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
