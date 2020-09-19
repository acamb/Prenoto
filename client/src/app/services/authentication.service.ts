import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import { getServer } from './Utils';
import * as jwt_decode from 'jwt-decode'
import {BehaviorSubject, Observable} from "rxjs";
import {User} from "../model/User";
import {map} from "rxjs/operators";
import {UserService} from "./user.service";
import {AppStateService} from "./app-state.service";


@Injectable()
export class AuthenticationService {

  private subject = new BehaviorSubject<User>(undefined);

  get user(): User {
    return JSON.parse( sessionStorage.getItem('user') );
  }

  get userObservable(): Observable<User> {
    return this.subject.asObservable();
  }


  get authenticated(): boolean {
    const loggedIn = sessionStorage.getItem('user') !== undefined;
    if (!loggedIn) {
      return false;
    }
    const date = new Date(0);
    try {
      const exp = jwt_decode(sessionStorage.getItem('token')).exp;
      date.setUTCSeconds(exp);
      const valid = new Date().valueOf() < date.valueOf()
      return valid;
    }
    catch(error){
      return false;
    }
  }

  get token(){
    return this.authenticated ? sessionStorage.getItem('token') : undefined;
  }

  logout(){
    sessionStorage.removeItem('user');
    sessionStorage.removeItem('token');
    this.subject.next(undefined);
    this.goToLogin();
  }



  constructor(private httpClient: HttpClient, private router: Router,private userService: UserService,private appState: AppStateService) {
    this.subject.next(JSON.parse( sessionStorage.getItem('user')));
   }

  authenticate(username: string, password: string): Observable<boolean>{
    return this.httpClient.post<AuthResponse>(getServer() + 'login', {username: username, password: password})
      .pipe(
      map(resp => {
        if (resp.access_token === undefined){
          return false;
        }
        sessionStorage.setItem('username', JSON.stringify(resp.username));
        sessionStorage.setItem('token', 'Bearer ' + resp.access_token);
        this.userService.getUser().subscribe(user => {
            sessionStorage.setItem('user', JSON.stringify(user));
            this.subject.next(user);
        })
        return true;
      },error => {
        return false;
      })
      )
  }

  goToLogin() {
    this.router.navigateByUrl('/authenticate');
  }
}

interface AuthResponse {
  access_token: string;
  username: string;
}
