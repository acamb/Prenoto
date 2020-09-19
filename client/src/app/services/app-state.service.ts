import { Injectable } from '@angular/core';
import {Settimana} from "../model/Settimana";
import {IscrizioneUtente} from "../model/IscrizioneUtente";
import {PrenotazioniService} from "./prenotazioni.service";
import {User} from "../model/User";

@Injectable({
  providedIn: 'root'
})
export class AppStateService {

  settimana: Settimana;
  iscrizioni: Array<IscrizioneUtente>;

  constructor(private prenotazioniService: PrenotazioniService) { }

  async loadIscrizioni() {
    this.iscrizioni = await this.prenotazioniService.getPrenotazioniUtente().toPromise();
  }

  async loadSettimana(){
    this.settimana =  await this.prenotazioniService.getSlotSettimana().toPromise();
  }
}
