import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ChampionshipBetComponent } from './component/championship-bet/championship-bet.component';
import { DriverComponent } from './component/driver/driver.component';
import { HomeComponent } from './component/home/home.component';
import { LoginComponent } from './component/login/login.component';
import { BetComponent } from './component/bet/bet.component';
import { BetPageComponent } from './page/bet-page/bet-page.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent},
  { path: 'driver', component: DriverComponent},
  { path: 'bets', component: BetPageComponent},
  { path: 'bet/:id', component: BetComponent},
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