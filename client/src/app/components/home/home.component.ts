import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Settimana} from "../../model/Settimana";
import {IscrizioneUtente} from "../../model/IscrizioneUtente";
import {AppStateService} from "../../services/app-state.service";
import {PrenotazioniService} from "../../services/prenotazioni.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  get settimana(){
    return this.appState.settimana
  }
  get iscrizioni(){
    return this.appState.iscrizioni
  }

  constructor(private route: ActivatedRoute,private appState: AppStateService,private prenotazioniService: PrenotazioniService) {
  }

  ngOnInit(): void {
  }

  async eliminaIscrizione(id){
    await this.prenotazioniService.cancella(id).toPromise();
    await this.appState.loadIscrizioni();
  }

}
