import { User } from './../../model/User';
import { from, Observable } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import {getGiornoFromNumero} from "../../services/Utils";
import {ActivatedRoute, Router} from "@angular/router";
import {AppStateService} from "../../services/app-state.service";
import {PrenotazioniService} from "../../services/prenotazioni.service";
import {Slot} from "../../model/Slot";
import {AuthenticationService} from "../../services/authentication.service";
import {ConfigTokens} from "../../model/Configurazione";

@Component({
  selector: 'app-iscrizione',
  templateUrl: './iscrizione.component.html',
  styleUrls: ['./iscrizione.component.scss']
})
export class IscrizioneComponent implements OnInit {

  giorno: number;
  slotScelto: Slot;
  ore: number = undefined;
  oreList : Array<number>;
  showSlotNelPassatoToast = false;
  admin=false;
  utenti: User[];
  forza=false;
  utente: User;

  get slotDisponibili() : Array<Slot>{
    return this.appState.settimana.giorni.find(g => g.giorno == this.giorno).slots.filter(s => s.posti > 0);
  }


  constructor(private route: ActivatedRoute,
              private authService: AuthenticationService,
              private appState: AppStateService,
              private prenotazioniService: PrenotazioniService,
              private router: Router) {
    this.giorno = route.snapshot.params.giorno
    if(!this.giorno || !this.appState.settimana){
      this.router.navigateByUrl("/")
    }
    this.oreList=[];
    let oreConf = <number><unknown>appState.parametri.find(p => p.chiave==ConfigTokens.NUMERO_ORE_MAX).valore
    for(let i = 1;i<=oreConf;i++){
      this.oreList.push(i);
    }
    if(this.oreList.length > 0) {
      this.ore = this.oreList[0];
    }
    this.admin = ["ADMIN","OPERATOR"].find((r)=> r === this.authService.user.role) != undefined;
    this.appState.loadUtentiNoCache().then(()=> this.utenti=this.appState.utenti);
  }

  getGiornoFromNumero=getGiornoFromNumero;

  ngOnInit(): void {
  }

  async onSubmit(){
    if(!this.slotScelto || this.ore === undefined || this.ore <0 ){
      return false
    }
    if(this.slotScelto.dataOraSlot < new Date()){
      this.showSlotNelPassatoToast=true
      return false
    }
    try {
      let result = await this.prenotazioniService.iscrivi(this.utente ? this.getUtenteFromList(this.utente) : this.authService.user, this.slotScelto, this.ore,this.utente != null).toPromise();
      if(result.success) {
        return this.router.navigateByUrl("/");
      }
      else{
        if(result.message == "PRENOTAZIONE_KO_CONCURRENT"){
          this.onSubmit();
        }
        else {
          return this.router.navigateByUrl(`/error/${result.message}`);
        }
      }
    }
    catch (e) {
      return this.router.navigateByUrl("/error");
    }

  }

  getUtenteFromList(utente): User{
    return this.utenti.find((u)=> (u.cognome + " " + u.nome) === utente)
  }
}
