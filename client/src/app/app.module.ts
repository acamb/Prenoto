import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from "@angular/common/http";
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
import { IscrizioneComponent } from './components/iscrizione/iscrizione.component';
import {BackButtonComponent} from "./components/back-button/back-button.component";
import { ErrorComponent } from './components/error/error.component';
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import { PostiRiservatiComponent } from './components/posti-riservati/posti-riservati.component';
import { AdminComponent } from './components/admin/admin.component';
import { PostoRiservatoTableComponent } from './components/posto-riservato-table/posto-riservato-table.component';
import {PostiResolver} from "./resolvers/PostiResolver";
import {UsersResolver} from "./resolvers/UsersResolver";
import {ParametriResolver} from "./resolvers/ParametriResolver";
import { CambioPasswordComponent } from './components/cambio-password/cambio-password.component';

// AoT requires an exported function for factories
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    RestrictedRoleDirective,
    IscrizioniComponent,
    SettimanaComponent,
    IscrizioneComponent,
    BackButtonComponent,
    ErrorComponent,
    PostiRiservatiComponent,
    AdminComponent,
    PostoRiservatoTableComponent,
    CambioPasswordComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    NgbModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      },
      defaultLanguage: 'it'
    })
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: JwtInterceptorService,
    multi: true
  },
    AuthenticationService,
    AuthGuard,
    IscrizioniUtenteResolver,
    SettimanaResolver,
    PostiResolver,
    UsersResolver,
    ParametriResolver
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
