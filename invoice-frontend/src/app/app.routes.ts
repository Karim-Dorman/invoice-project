import { Routes } from '@angular/router';
import { ProfileComponent } from './components/profile/profile.component';
import { InvoicesdashboardComponent } from './components/invoicesdashboard/invoicesdashboard.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { authGuard } from './auth/auth.guard';
import { loginguardGuard } from './auth/loginguard.guard';
import { ChangePasswordComponent } from './components/change-password/change-password.component';

export const routes: Routes = [
  { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
  { path: 'invoices', component: InvoicesdashboardComponent, canActivate: [authGuard] },
  { path: 'login', component: LoginComponent, canActivate: [loginguardGuard] },
  { path: 'register', component: RegisterComponent },
  { path: 'change-password', component: ChangePasswordComponent, canActivate:[authGuard] }
];
