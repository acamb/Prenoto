import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import { Observable } from 'rxjs';
import {AppStateService} from "../services/app-state.service";
import {AuthenticationService} from "../services/authentication.service";

@Injectable({
  providedIn: 'root'
})
export class PasswordTemporaneaGuard implements CanActivate {

  constructor(private authService: AuthenticationService,private router: Router) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if(this.authService.user && !this.authService.user.cambioPassword) {
      return true;
    }
    else{
      this.router.navigateByUrl("/password");
      return false;
    }
  }

}
