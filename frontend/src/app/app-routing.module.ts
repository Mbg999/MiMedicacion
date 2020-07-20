import { HomeComponent } from './components/home/home.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// GUARDS
import { IsLoggedGuard } from './guards/is-logged.guard';
import { AuthGuard } from './guards/auth.guard';

// COMPONENTS
import { AuthComponent } from './components/auth/auth.component';


const routes: Routes = [
  { path: 'auth', component: AuthComponent, canActivate: [IsLoggedGuard] },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/auth', pathMatch: 'full' },
  { path: '**', component: AuthComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
