import { Component, OnInit } from '@angular/core';
import {getGiornoFromNumero} from "../../services/Utils";
import {ActivatedRoute, Router} from "@angular/router";
import {AppStateService} from "../../services/app-state.service";
import {PrenotazioniService} from "../../services/prenotazioni.service";
import {Slot} from "../../model/Slot";
import {AuthenticationService} from "../../services/authentication.service";

@Component({
  selector: 'app-iscrizione',
  templateUrl: './iscrizione.component.html',
  styleUrls: ['./iscrizione.component.scss']
})
export class IscrizioneComponent implements OnInit {

  giorno: number;
  slotScelto: Slot;
  ore: number;
  oreList = [1,2,3] //TODO[AC] da configurazione

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
  }

  getGiornoFromNumero=getGiornoFromNumero;

  ngOnInit(): void {
  }

  async onSubmit(){
    try {
      let result = await this.prenotazioniService.iscrivi(this.authService.user, this.slotScelto, this.ore).toPromise();
      console.log(JSON.stringify(result))
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
}
