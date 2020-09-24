import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../services/authentication.service";
import {Router} from "@angular/router";
import {AppStateService} from "../../services/app-state.service";

@Component({
  selector: 'app-cambio-password',
  templateUrl: './cambio-password.component.html',
  styleUrls: ['./cambio-password.component.scss']
})
export class CambioPasswordComponent implements OnInit {

  oldPassword: string;
  newPassword: string;
  rePassword: string;
  error:string;

  constructor(private authService : AuthenticationService,private router : Router,private appStateService: AppStateService) { }

  ngOnInit(): void {
  }

  async onSubmit(){
    if(this.newPassword != this.rePassword){
      this.error = "err.new.password"
    }
    else{
      let res = await this.authService.cambioPassword(this.oldPassword,this.newPassword).toPromise();
      if(res && res.success){
        this.authService.logout();
        this.router.navigateByUrl("/");
      }
      else{
        this.error = res?.message ? res.message : 'generic.error'
      }
    }
  }

}
