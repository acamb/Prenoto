import { Component, OnInit } from '@angular/core';
import {AdminService} from "../../services/admin.service";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  constructor(private adminService: AdminService) { }

  info = ""
  error = ""

  ngOnInit(): void {
  }

  async ricrea(){
    let apiResponse = await this.adminService.ricreaSettimana().toPromise();
    if(apiResponse.success){
      this.info = "settimana creata con successo"
    }
    else{
      this.error = "Si e' verificato un errore: " + apiResponse.message;
    }
  }

}
