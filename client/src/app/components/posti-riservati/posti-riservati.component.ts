import { Component, OnInit } from '@angular/core';
import {PostoRiservato} from "../../model/PostoRiservato";
import {User} from "../../model/User";
import {AppStateService} from "../../services/app-state.service";
import {getGiornoFromNumero} from "../../services/Utils";
import {PostiRiservatiService, TipoIscrizione} from "../../services/posti-riservati.service";

@Component({
  selector: 'app-posti-riservati',
  templateUrl: './posti-riservati.component.html',
  styleUrls: ['./posti-riservati.component.scss']
})
export class PostiRiservatiComponent implements OnInit {

  getGiornoFromNumero= getGiornoFromNumero;
  tipiPrenotazione = [TipoIscrizione.Ufficio,TipoIscrizione.Preferenza]

  tipo: TipoIscrizione;
  giorno: number;
  ora: number;
  orePrenotazione: number;
  user: User; //TODO[AC] fare il type-ahead

  ore = [9,10,11,12,13,14,15,16,17,18] //TODO[AC] da configurazione
  oreMax = [1,2,3] //TODO[AC] da configurazione

  get riservatiUfficio() : Array<PostoRiservato>{
    return this.appState.postiRiservatiUfficio;
}
  get riservatiPrenotazione() : Array<PostoRiservato>{
    return this.appState.postiRiservatiPreferenza;
  }
  get utenti(): Array<User> {
    return this.appState.utenti;
  }

  constructor(private appState : AppStateService,private postiRiservatiService: PostiRiservatiService) { }

  ngOnInit(): void {
  }

  onSubmit() {
    this.postiRiservatiService.
  }
}
