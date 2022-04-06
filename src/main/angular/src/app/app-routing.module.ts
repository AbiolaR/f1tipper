import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { LoginComponent } from './component/login/login.component';
import { BetComponent } from './component/bet/bet.component';
import { BetPageComponent } from './page/bet-page/bet-page.component';
import { RaceToBetRedirectComponent } from './component/race-to-bet-redirect/race-to-bet-redirect.component';
import { UserDialogComponent } from './component/dialog/user-dialog/user-dialog.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent},
  { path: '', redirectTo: 'home', pathMatch: 'full'},
  { path: 'bets', component: BetPageComponent},
  { path: 'bet/:id', component: BetComponent},
  { path: 'race/:id', component: RaceToBetRedirectComponent },
  { path: 'login', component: LoginComponent},
  { path: 'user', component: UserDialogComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {enableTracing: true, onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const RoutingComponents = [HomeComponent] 