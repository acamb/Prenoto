import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {IscrizioneUtente} from "../model/IscrizioneUtente";
import {Observable} from "rxjs";
import {AuthenticationService} from "../services/authentication.service";
import {PrenotazioniService} from "../services/prenotazioni.service";
import {Settimana} from "../model/Settimana";
import {Injectable} from "@angular/core";
import {AppStateService} from "../services/app-state.service";
@Injectable()
export class SettimanaResolver implements Resolve<Settimana> {

  constructor(private authService: AuthenticationService,private appState: AppStateService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> | any {
      return this.appState.loadSettimana()
  }

}
