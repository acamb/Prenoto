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
      this.router.navigateByUrl("/")
    }
  }

  async onSubmit() {
    let result = await this.authService.authenticate(this.username, this.password)
    if (result) {
      this.router.navigateByUrl("/")
    } else {
      this.wrongLogin = true;
    }
  }
}
