import {Injectable} from '@angular/core';
import {Settimana} from "../model/Settimana";
import {IscrizioneUtente} from "../model/IscrizioneUtente";
import {PrenotazioniService} from "./prenotazioni.service";
import {User} from "../model/User";
import {PostoRiservato} from "../model/PostoRiservato";
import {PostiRiservatiService, TipoIscrizione} from "./posti-riservati.service";
import {UserService} from "./user.service";

@Injectable({
  providedIn: 'root'
})
export class AppStateService {

  settimana: Settimana;
  iscrizioni: Array<IscrizioneUtente>;
  postiRiservatiUfficio: Array<PostoRiservato>;
  postiRiservatiPreferenza: Array<PostoRiservato>;
  utenti: Array<User>;

  constructor(private prenotazioniService: PrenotazioniService,
              private postiRiservatiService: PostiRiservatiService,
              private userService: UserService) { }

  async loadIscrizioni() {
    this.iscrizioni = await this.prenotazioniService.getPrenotazioniUtente().toPromise();
  }

  async loadSettimana(){
    this.settimana =  await this.prenotazioniService.getSlotSettimana().toPromise();
  }

  async loadPostiUfficio(){
    this.postiRiservatiUfficio = await this.postiRiservatiService.getPostiPerTipo(TipoIscrizione.Ufficio).toPromise();
  }

  async loadPostiPreferenza(){
    this.postiRiservatiPreferenza = await this.postiRiservatiService.getPostiPerTipo(TipoIscrizione.Preferenza).toPromise();
  }

  async loadUtenti(){
    if(this.utenti && this.utenti.length > 0){
      return;
    }
    this.utenti = await this.userService.geAllUsers().toPromise();
  }

  async loadPosti() {
     this.loadPostiUfficio();
     this.loadPostiPreferenza();
  }
}
