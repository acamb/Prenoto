import { Component } from '@angular/core';
import {AuthenticationService} from "./services/authentication.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  isNavbarCollapsed = true;

  constructor(private authService: AuthenticationService) {
  }

  logout(){
    this.authService.logout();
  }
}
