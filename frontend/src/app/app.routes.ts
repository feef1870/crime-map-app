import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';
import { MapComponent } from './map/map.component';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: 'login', component: Login },

  { path: 'register', component: Register },

  { path: 'map', component: MapComponent, canActivate: [authGuard] },

  { path: '', redirectTo: 'map', pathMatch: 'full' },
];
