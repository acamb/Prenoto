import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {User} from "../model/User";
import {IscrizioneUtente} from "../model/IscrizioneUtente";
import {HttpClient} from "@angular/common/http";
import {getServer} from "./Utils";
import {Settimana} from "../model/Settimana";
import {Slot} from "../model/Slot";
import {ApiResult} from "../model/ApiResult";

@Injectable({
  providedIn: 'root'
})
export class PrenotazioniService {

  constructor(private http: HttpClient) {

  }

  getPrenotazioniUtente() : Observable<Array<IscrizioneUtente>> {
    return this.http.get<Array<IscrizioneUtente>>(getServer() + "api/prenotazione");
  }

  getSlotSettimana() : Observable<Settimana>{
    return this.http.get<Settimana>(getServer() + "api/prenotazione/slotAttivi");
  }

  iscrivi(user: User,slot: Slot,ore: number): Observable<ApiResult>{
    return this.http.post<ApiResult>(getServer() + "api/prenotazione",{
        user: user,
        slot: {
            id: slot.id
        },
        ore: ore
    });
  }

  cancella(id: number) : Observable<ApiResult> {
    return this.http.delete<ApiResult>(getServer() + `api/prenotazione?id=${id}`)
  }
}
