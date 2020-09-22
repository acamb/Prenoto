import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {AuthGuard} from "./guards/AuthGuard";
import {HomeComponent} from "./components/home/home.component";
import {IscrizioniUtenteResolver} from "./resolvers/IscrizioniUtenteResolver";
import {SettimanaResolver} from "./resolvers/SettimanaResolver";
import {IscrizioneUtente} from "./model/IscrizioneUtente";
import {IscrizioneComponent} from "./components/iscrizione/iscrizione.component";
import {ErrorComponent} from "./components/error/error.component";
import {PostiRiservatiComponent} from "./components/posti-riservati/posti-riservati.component";
import {AdminComponent} from "./components/admin/admin.component";
import {PostiResolver} from "./resolvers/PostiResolver";
import {UsersResolver} from "./resolvers/UsersResolver";


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
        path: 'iscrivi/:giorno',
        component: IscrizioneComponent
      },
      {
        path: 'error/:code',
        component: ErrorComponent
      },
      {
        path: 'postiRiservati',
        component: PostiRiservatiComponent,
        resolve: {
          posti: PostiResolver,
          users: UsersResolver
        }
      },
      {
        path: 'admin',
        component: AdminComponent
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
