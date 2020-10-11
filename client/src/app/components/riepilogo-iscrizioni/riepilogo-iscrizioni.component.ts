import { Component, OnInit } from '@angular/core';
import {PrenotazioniService} from "../../services/prenotazioni.service";
import {AppStateService} from "../../services/app-state.service";
import {Settimana} from "../../model/Settimana";
import {Giorno} from "../../model/Giorno";
import {Slot} from "../../model/Slot";
import {getGiornoFromNumero} from "../../services/Utils";

@Component({
  selector: 'app-riepilogo-iscrizioni',
  templateUrl: './riepilogo-iscrizioni.component.html',
  styleUrls: ['./riepilogo-iscrizioni.component.scss']
})
export class RiepilogoIscrizioniComponent implements OnInit {

  getGiornoFromNumero = getGiornoFromNumero

  get settimana(): Settimana{
    return this.appState.settimana
  }

  getGiorniConIscrizioni(): Array<Giorno>{
    return this.settimana
      ?.giorni.filter(g => g.slots
                              .filter(s => s.iscritti && s.iscritti.length>0).length > 0
                     )
  }

  getSlotConIscrittiPerGiorno(giorno: number) : Array<Slot>{
    return this.settimana.giorni
          .find(g => g.giorno == giorno)
          .slots
          .filter(s => s.iscritti && s.iscritti.length > 0);
  }

  constructor(private prenotazioniService: PrenotazioniService,private appState: AppStateService) { }

  ngOnInit(): void {
  }

}
