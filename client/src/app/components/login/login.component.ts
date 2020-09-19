import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../services/authentication.service";
import {Router} from "@angular/router";
import {map, take} from "rxjs/operators";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  username: string;
  password: string;
  wrongLogin: boolean;

  constructor(private router: Router,private authService: AuthenticationService) { }

  ngOnInit() {
    if(this.authService.authenticated){
      //TODO[AC]
      this.router.navigateByUrl("/")
    }
  }

  onSubmit(){
    this.authService.authenticate(this.username,this.password).pipe(map(result => {
      if(result){
        //TODO[AC]
        this.router.navigateByUrl("/")
      }
      else{
        this.wrongLogin=true;
      }
    }),
      take(1)
    ).subscribe();
  }

}
