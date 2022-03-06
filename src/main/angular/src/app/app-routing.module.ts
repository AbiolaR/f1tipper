import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChampionshipBetComponent } from './component/championship-bet/championship-bet.component';
import { DriverComponent } from './component/driver/driver.component';
import { HomeComponent } from './component/home/home.component';
import { LoginComponent } from './component/login/login.component';
import { RaceBetComponent } from './component/race-bet/race-bet.component';
import { RaceBetsComponent } from './component/race-bets/race-bets.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent},
  { path: 'driver', component: DriverComponent},
  { path: 'race-bets', component: RaceBetsComponent},
  { path: 'race-bet/:id', component: RaceBetComponent},
  { path: 'championship', component: ChampionshipBetComponent},
  { path: 'login', component: LoginComponent},
  { path: '**', component: HomeComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {enableTracing: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const RoutingComponents = [DriverComponent, HomeComponent] 