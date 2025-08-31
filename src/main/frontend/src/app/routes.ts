import { Routes } from '@angular/router';
import { LoginComponent } from './components/login.component';
import { DashboardComponent } from './components/dashboard.component';
import { AccountsComponent } from './components/accounts.component';
import { TransferComponent } from './components/transfer.component';
import { CardsComponent } from './components/cards.component';
import { ProfileComponent } from './components/profile.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', component: DashboardComponent },
  { path: 'accounts', component: AccountsComponent },
  { path: 'transfer', component: TransferComponent },
  { path: 'cards', component: CardsComponent },
  { path: 'profile', component: ProfileComponent },
  { path: '**', redirectTo: '' }
];
