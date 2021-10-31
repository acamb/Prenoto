import { Component, OnInit } from '@angular/core';
import { ActivationEnd, Router } from '@angular/router';
import { faCheck, faEdit, faKey, faTimes } from '@fortawesome/free-solid-svg-icons';
import { User } from 'src/app/model/User';
import { AppStateService } from 'src/app/services/app-state.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-gestione-utenti',
  templateUrl: './gestione-utenti.component.html',
  styleUrls: ['./gestione-utenti.component.scss']
})
export class GestioneUtentiComponent implements OnInit {

  faCheck = faCheck;
  faX = faTimes;
  faEdit = faEdit;
  faKey = faKey

  filter = ""

  page = 1;
  pageSize = 5;

  constructor(private appState: AppStateService,private router: Router,private userService: UserService) {
      
   }

  ngOnInit(): void {
    this.refresh();
  }

  async refresh(){
    await this.appState.loadUtentiNoCache();
  }

  onRowClick(itemId: number){
      this.router.navigateByUrl(`/utente/${itemId}`)
  }

  async onPasswordReset(id: number){
      this.router.navigateByUrl(`resetPassword/${id}`)
  }

  get users() {
    if(!this.filter){
      return this.appState.utenti
        ?.slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
    }
    else{
      return this.appState.utenti.filter((u) => u.username.indexOf(this.filter)!= -1)
      ?.slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
    }
  }

  get collectionSize(): number{
    return this.appState.utenti?.length;
  }

}
