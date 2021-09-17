import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {IscrizioneUtente} from "../model/IscrizioneUtente";
import {Observable} from "rxjs";
import {AuthenticationService} from "../services/authentication.service";
import {PrenotazioniService} from "../services/prenotazioni.service";
import {Injectable} from "@angular/core";
import {AppStateService} from "../services/app-state.service";
import { UserService } from "../services/user.service";
import { User } from "../model/User";

@Injectable()
export class UserResolver implements Resolve<User> {

  constructor(private userService: UserService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<User> | Promise<User> | User {
      return this.userService.loadUser(route.params.id).toPromise() 
  }

}
