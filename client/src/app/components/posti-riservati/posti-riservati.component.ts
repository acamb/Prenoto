import {Component, OnInit} from '@angular/core';
import {PostoRiservato} from "../../model/PostoRiservato";
import {User} from "../../model/User";
import {AppStateService} from "../../services/app-state.service";
import {getGiornoFromNumero} from "../../services/Utils";
import {PostiRiservatiService, TipoIscrizione} from "../../services/posti-riservati.service";
import {Observable} from "rxjs";
import {debounceTime, distinctUntilChanged, filter, map} from "rxjs/operators";

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
  user: User;

  ore = [9,10,11,12,13,14,15,16,17,18] //TODO[AC] da configurazione
  oreMax = [1,2,3] //TODO[AC] da configurazione
  userFormatter = (user: User) => user.cognome + " " + user.nome;

  searchUser = (text$: Observable<string>) => text$.pipe(
    debounceTime(200),
    distinctUntilChanged(),
    filter(term => term.length >= 2),
    map(term => this.appState.utenti.filter(user => new RegExp(term, 'mi').test(user.cognome + " " + user.nome)).slice(0, 10))
  )
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

  async onSubmit() {
    let postoRiservato = new PostoRiservato()
    postoRiservato.giorno = this.giorno
    postoRiservato.userId=this.user.id
    postoRiservato.numeroOre = this.orePrenotazione
    postoRiservato.ora = this.ora
    postoRiservato.tipoIscrizione=this.tipo
    await this.postiRiservatiService.salvaPosto(postoRiservato,this.user).toPromise();
    if(this.tipo == TipoIscrizione.Preferenza) {
      await this.appState.loadPostiPreferenza();
    }
    else {
      await this.appState.loadPostiUfficio();
    }
    this.giorno = undefined;
    this.user = undefined;
    this.orePrenotazione = undefined;
    this.ora = undefined;
    this.tipo = undefined;
  }

  async cancella(posto: PostoRiservato){
    await this.postiRiservatiService.cancellaPosto(posto).toPromise();
    if(posto.tipoIscrizione == TipoIscrizione.Preferenza){
      await this.appState.loadPostiPreferenza();
    }
    else{
      await this.appState.loadPostiUfficio();
    }

  }


}
