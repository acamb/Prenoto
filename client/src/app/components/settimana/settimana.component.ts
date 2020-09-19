import {Component, Input, OnInit} from '@angular/core';
import {Settimana} from "../../model/Settimana";
import {Slot} from "../../model/Slot";
import {getGiornoFromNumero} from "../../services/Utils";
import {Router} from "@angular/router";

@Component({
  selector: 'app-settimana',
  templateUrl: './settimana.component.html',
  styleUrls: ['./settimana.component.scss']
})
export class SettimanaComponent implements OnInit {

  @Input()
  settimana: Settimana

  getGiornoFromNumero = getGiornoFromNumero;

  constructor(private router: Router) {
  }

  get giorni() : Array<number> {
    return this.settimana.giorni.filter(
          g => g.slots.filter(s => s.visibile).length > 0) //solo quelli con slot ancora liberi
      .map(g => g.giorno)
  }

  getSlots(giorno: number) : Array<Slot> {
    return this.settimana.giorni.find(g => g.giorno == giorno).slots.filter(s => s.posti > 0)
  }


  ngOnInit(): void {
  }

  iscrivi(giorno: number){
    this.router.navigateByUrl(`/iscrivi/${giorno}`);
  }

}
