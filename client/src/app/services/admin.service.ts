import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ApiResult} from "../model/ApiResult";
import {getServer} from "./Utils";
import {Configurazione} from "../model/Configurazione";

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) {

  }

  ricreaSettimana() : Observable<ApiResult>{
    return this.http.post<ApiResult>(getServer() + "api/admin/creaSettimana",{})
  }

  getConfigurazione(): Observable<Array<Configurazione>>{
    return this.http.get<Array<Configurazione>>(getServer() + "api/admin/parametri",{})
  }
}
