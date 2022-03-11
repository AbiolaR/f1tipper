import { HttpClientModule } from '@angular/common/http';
import { ErrorHandler, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

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
import { RaceBetsComponent } from './component/race-bets/race-bets.component';
import { RaceBetComponent } from './component/race-bet/race-bet.component';
import { RaceBetItemComponent } from './component/race-bet-item/race-bet-item.component';
import { ChampionshipBetComponent } from './component/championship-bet/championship-bet.component';
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

@NgModule({
  declarations: [
    AppComponent,
    RoutingComponents,
    NavBarComponent,
    RaceBetsComponent,
    RaceBetComponent,
    RaceBetItemComponent,
    ChampionshipBetComponent,
    BetItemDialogComponent,
    LoginComponent,
    AccessDeniedDialogComponent,
    LeagueDialogComponent,    
  ],
  imports: [
    BrowserModule,
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
    ServiceWorkerModule.register('ngsw-worker.js', {
      enabled: environment.production,
      // Register the ServiceWorker as soon as the app is stable
      // or after 30 seconds (whichever comes first).
      registrationStrategy: 'registerWhenStable:30000'
    }),
    BrowserAnimationsModule
  ],
  providers: [{
    provide: ErrorHandler,
    useClass: GlobalErrorHandler
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
