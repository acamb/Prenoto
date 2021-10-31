import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/model/User';
import { AppStateService } from 'src/app/services/app-state.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-reset-password-confirm',
  templateUrl: './reset-password-confirm.component.html',
  styleUrls: ['./reset-password-confirm.component.scss']
})
export class ResetPasswordConfirmComponent implements OnInit {

  user: User;

  constructor(private appState: AppStateService,private activeRoute: ActivatedRoute,private userService: UserService,private router:Router) { 
    this.user = activeRoute.snapshot.data.user;
  }

  ngOnInit(): void {
  }

  async conferma(){
    await this.userService.passwordReset(this.user.id).toPromise()
    this.router.navigateByUrl("/utenti");
  }

}
