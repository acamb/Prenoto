import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot} from '@angular/router';
import {AuthenticationService} from "../services/authentication.service";
import {Observable} from "rxjs";

@Injectable()
export class AuthGuard implements CanActivate, CanActivateChild {

  constructor( private authService: AuthenticationService, private router: Router ) {
  }

  canActivate( route: ActivatedRouteSnapshot, state: RouterStateSnapshot ) {
    if ( this.authService.authenticated ) {
      if (route.data.roles === undefined) {
        return true;
      }
      else {
        return route.data.roles.indexOf(this.authService.user.role) !== -1
      }
    }
    else {
      this.authService.logout();
      return false;
    }
  }


  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    return this.canActivate(childRoute, state);
  }
}
