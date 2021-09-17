import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../model/User";
import {getServer} from "./Utils";
import { ApiResult } from '../model/ApiResult';

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
    return this.http.get<Array<User>>(getServer()+"api/user/list?rnd="+Math.random())
  }

  passwordReset(id: number): Observable<ApiResult>{
    return this.http.post<ApiResult>(getServer()+"api/user/resetPassword",{id: id});
  }

  updateUSer(user: User): Observable<ApiResult>{
    return this.http.post<ApiResult>(getServer()+"api/user/aggiorna",{
      id : user.id,
      nome : user.nome,
      cognome : user.cognome,
      active : user.active,
      dataFineValiditaGreenPass : user.dataFineValiditaGreenPass,
      dataFineVisitaAgonistica : user.dataFineVisitaAgonistica
    });
  }

  loadUser(id: number) : Observable<User> {
    return this.http.get<User>(getServer()+`api/user/get?id=${id}`)
  }
}
