import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../model/User";
import {getServer} from "./Utils";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUser() : Observable<User> {
    return this.http.get<User>(getServer()+"api/user")
  }

  getUserFromId(id: number) : Observable<User> {
    return this.http.get<User>(getServer()+"api/user/"+id)
  }

  geAllUsers(): Observable<Array<User>> {
    return this.http.get<Array<User>>(getServer()+"api/user/list")
  }
}
