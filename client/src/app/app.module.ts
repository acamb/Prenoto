import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {JwtInterceptorService} from "./services/jwt-interceptor.service";
import {AuthenticationService} from "./services/authentication.service";
import {AuthGuard} from "./guards/AuthGuard";
import { LoginComponent } from './components/login/login.component';
import {FormsModule} from "@angular/forms";
import { HomeComponent } from './components/home/home.component';
import {IscrizioniUtenteResolver} from "./resolvers/IscrizioniUtenteResolver";
import {SettimanaResolver} from "./resolvers/SettimanaResolver";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {RestrictedRoleDirective} from "./directives/restricted-role.directive";
import { IscrizioniComponent } from './components/iscrizioni/iscrizioni.component';
import { SettimanaComponent } from './components/settimana/settimana.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    RestrictedRoleDirective,
    IscrizioniComponent,
    SettimanaComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    NgbModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: JwtInterceptorService,
    multi: true
  },
    AuthenticationService,
    AuthGuard,
    IscrizioniUtenteResolver,
    SettimanaResolver

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
