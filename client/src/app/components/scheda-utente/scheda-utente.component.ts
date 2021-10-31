import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/model/User';
import { AppStateService } from 'src/app/services/app-state.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-scheda-utente',
  templateUrl: './scheda-utente.component.html',
  styleUrls: ['./scheda-utente.component.scss']
})
export class SchedaUtenteComponent implements OnInit {

  user: User;

  constructor(private appState: AppStateService,private activeRoute: ActivatedRoute,private userService: UserService,private router:Router) { 
    this.user = activeRoute.snapshot.data.user;
  }

  ngOnInit(): void {
  }

  async onSubmit(){
    await this.userService.updateUSer(this.user).toPromise();
    await this.appState.loadUtenti();
    this.router.navigateByUrl("/utenti");
  }

}
