import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {IscrizioneUtente} from "../../model/IscrizioneUtente";
import {getGiornoFromNumero} from "../../services/Utils";

@Component({
  selector: 'app-iscrizioni',
  templateUrl: './iscrizioni.component.html',
  styleUrls: ['./iscrizioni.component.scss']
})
export class IscrizioniComponent implements OnInit {

  @Input()
  iscrizioni: Array<IscrizioneUtente>
  @Output()
  eliminaEmitter = new EventEmitter<number>()
  @Output()
  eliminaTutteEmitter = new EventEmitter<Array<number>>()

  getGiornoFromNumero = getGiornoFromNumero;

  get giorni(){
    return new Set(this.iscrizioni.map(i => i.giorno))
  }

  getOre(giorno: number): Array<IscrizioneUtente> {
    return this.iscrizioni.filter(i => i.giorno == giorno)
  }

  constructor() { }

  ngOnInit(): void {
  }

  elimina(id: number){
    this.eliminaEmitter.emit(id);
  }

  eliminaGiorno(giorno: number){
    let ids = []
    for(let id of this.getOre(giorno).map(ora => ora.id)){
      ids.push(id)
    }
    this.eliminaTutteEmitter.emit(ids);
  }

  canDelete(iscrizione: IscrizioneUtente,giorno: number){
    let iscrizioniList = this.getOre(giorno)
    if(iscrizioniList.findIndex( i => i.id == iscrizione.id) === 0){
      return true;
    }
    if(iscrizioniList.findIndex( i => i.id == iscrizione.id) === iscrizioniList.length - 1){
      return true;
    }
    return false;
  }

}
