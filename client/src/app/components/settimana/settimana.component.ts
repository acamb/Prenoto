import { ConfigTokens } from './../../model/Configurazione';
import { AppStateService } from './../../services/app-state.service';
import { HourlyTrend } from './../../model/DailyTrend';
import { TrendService } from './../../services/trend.service';
import { range } from './../../services/Utils';
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
import { DailyTrend } from 'src/app/model/DailyTrend';
import { A } from 'chart.js/dist/chunks/helpers.core';

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

  trendModalVisible = false;

  trendData: Observable<DailyTrend>;
  trendLabels = range(9,19);
  oreMax: number;

  getGiornoFromNumero = getGiornoFromNumero;

  constructor(private router: Router,
    private prenotazioniService : PrenotazioniService,
    private trendService: TrendService,
    private appStateService: AppStateService) {
      this.oreMax =  <number><unknown>appStateService.parametri.find(p => p.chiave=ConfigTokens.NUMERO_ORE_MAX).valore
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

  openTrend(giorno: number){
    this.trendData = this.trendService.getDailyTrend(giorno);
    this.trendModalVisible = true;
  }

  closeTrend(){
    this.trendData = null;
    this.trendModalVisible = false;
  }

  convertData(data: HourlyTrend[] ){
    return data?.map(x=>x.value);
  }
}
