import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {AuthGuard} from "./guards/AuthGuard";
import {HomeComponent} from "./components/home/home.component";
import {IscrizioniUtenteResolver} from "./resolvers/IscrizioniUtenteResolver";
import {SettimanaResolver} from "./resolvers/SettimanaResolver";


const routes: Routes = [
  {
    path: 'authenticate',
    component: LoginComponent
  },
  {
    path: '',
    canActivateChild: [AuthGuard],
    runGuardsAndResolvers: 'always',
    children: [
      {
        path: 'home',
        component: HomeComponent,
        resolve: {
          iscrizioni: IscrizioniUtenteResolver,
          settimana: SettimanaResolver
        }
      },
      {
        path: '',
        redirectTo: "home",
        pathMatch: 'full'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
