import {Component, Input, OnInit} from '@angular/core';
import {Settimana} from "../../model/Settimana";
import {Slot} from "../../model/Slot";
import {getGiornoFromNumero} from "../../services/Utils";
import {Router} from "@angular/router";
import {PrenotazioniService} from "../../services/prenotazioni.service";
import {map} from "rxjs/operators";
import {Observable} from "rxjs";
import {NgbPopover} from "@ng-bootstrap/ng-bootstrap";
import {Giorno} from "../../model/Giorno";

@Component({
  selector: 'app-settimana',
  templateUrl: './settimana.component.html',
  styleUrls: ['./settimana.component.scss']
})
export class SettimanaComponent implements OnInit {

  @Input()
  settimana: Settimana
  @Input()
  postiPerSlot: number

  getGiornoFromNumero = getGiornoFromNumero;

  constructor(private router: Router,private prenotazioniService : PrenotazioniService) {
  }

  get giorni() : Array<Giorno> {
    return this.settimana.giorni.filter(
          g => g.slots.filter(s => s.visibile).length > 0); //solo quelli con slot ancora liberi
  }

  getSlots(giorno: number) : Array<Slot> {
    return this.settimana.giorni.find(g => g.giorno == giorno).slots.filter(s => s.posti > 0)
  }


  ngOnInit(): void {
  }

  iscrivi(giorno: number){
    this.router.navigateByUrl(`/iscrivi/${giorno}`);
  }

  getUtenti(slot: Slot){
    return this.prenotazioniService.getUtentiIscritti(slot)
  }
}
