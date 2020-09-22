import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {PostoRiservato} from "../../model/PostoRiservato";
import {User} from "../../model/User";
import { getGiornoFromNumero } from 'src/app/services/Utils';

@Component({
  selector: 'app-posto-riservato-table',
  templateUrl: './posto-riservato-table.component.html',
  styleUrls: ['./posto-riservato-table.component.scss']
})
export class PostoRiservatoTableComponent implements OnInit,OnChanges {

  @Input()
  posti: Array<PostoRiservato>
  @Input()
  users: Array<User>
  postiFiltered: Array<PostoRiservato>
  page = 1;
  pageSize = 5;
  @Output()
  cancellaPosto = new EventEmitter<PostoRiservato>()

  getGiornoFromNumero = getGiornoFromNumero;
  get collectionSize(): number{
    return this.posti?.length;
  }

  constructor() {
    this.refresh();
  }

  ngOnChanges(changes: import("@angular/core").SimpleChanges): void {
        this.refresh();
    }



  ngOnInit(): void {
    this.refresh()
  }

  refresh() {
    this.postiFiltered = this.posti
      ?.slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
  }

  getUser(userId: number) : User {
    return this.users?.find(u => u.id == userId)
  }

  cancella(posto: PostoRiservato) {
    this.cancellaPosto.emit(posto);
  }
}
