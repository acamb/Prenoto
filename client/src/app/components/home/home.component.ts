import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Settimana} from "../../model/Settimana";
import {IscrizioneUtente} from "../../model/IscrizioneUtente";
import {AppStateService} from "../../services/app-state.service";
import {PrenotazioniService} from "../../services/prenotazioni.service";
import { ConfigTokens, Configurazione } from 'src/app/model/Configurazione';

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

  get postiPerSlot(){
    return this.appState.parametri.find(p => p.chiave==ConfigTokens.POSTI_PER_ORA).valore
  }

  constructor(private route: ActivatedRoute,
              private appState: AppStateService,
              private prenotazioniService: PrenotazioniService,
              private router: Router) {
  }

  ngOnInit(): void {
  }

  async eliminaIscrizione(id){
    let resp = await this.prenotazioniService.cancella(id).toPromise();
    if(resp.success){
      this.appState.loadIscrizioni();
      await this.appState.loadSettimana();
      return true;
    }
    else{
      this.router.navigateByUrl(`/error/${resp.message}`);
      return false;
    }
  }

  async eliminaIscrizioniGiorno(ids){
    let res = false;
    for(let id of ids){
      try {
        res = await this.eliminaIscrizione(id);
      }
      catch(e){
        res = false;
      }
      if(!res){
        break;
      }
    }
  }


}
