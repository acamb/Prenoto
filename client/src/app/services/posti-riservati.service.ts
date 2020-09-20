import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {IscrizioneUtente} from "../model/IscrizioneUtente";
import {getServer} from "./Utils";
import {PostoRiservato} from "../model/PostoRiservato";
import {User} from "../model/User";
import {ApiResult} from "../model/ApiResult";

@Injectable({
  providedIn: 'root'
})
export class PostiRiservatiService {

  constructor(private http: HttpClient) {

  }

  getPostiPerTipo(tipo: TipoIscrizione ) : Observable<Array<PostoRiservato>> {
    return this.http.get<Array<PostoRiservato>>(getServer() + "api/postoRiservato/" + tipo);
  }

  salvaPosto(posto: PostoRiservato,user: User) : Observable<ApiResult>{
    posto.userId = user.id
    return this.http.post<ApiResult>(getServer()+ "api/postoRiservato",posto);
  }

  cancellaPosto(posto: PostoRiservato) : Observable<ApiResult>{
    return this.http.delete<ApiResult>(getServer()+ "api/postoRiservato?id="+posto.id);
  }


}

export enum TipoIscrizione{
  Ufficio = "UFFICIO",
  Preferenza = "PREFERENZA",
  Utente = "UTENTE"
}
