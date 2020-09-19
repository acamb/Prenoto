import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {IscrizioneUtente} from "../model/IscrizioneUtente";
import {Observable} from "rxjs";
import {AuthenticationService} from "../services/authentication.service";
import {PrenotazioniService} from "../services/prenotazioni.service";
import {Injectable} from "@angular/core";
import {AppStateService} from "../services/app-state.service";

@Injectable()
export class IscrizioniUtenteResolver implements Resolve<Array<IscrizioneUtente>> {

  constructor(private authService: AuthenticationService,private appState: AppStateService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<any> | Promise<any> | any {
      return this.appState.loadIscrizioni();
  }

}
