import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ApiResult} from "../model/ApiResult";
import {getServer} from "./Utils";

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) {

  }

  ricreaSettimana() : Observable<ApiResult>{
    return this.http.post<ApiResult>(getServer() + "api/admin/creaSettimana",{})
  }
}
