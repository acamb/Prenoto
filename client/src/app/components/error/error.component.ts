import { Component, OnInit } from '@angular/core';
import {ActivatedRoute,Router} from "@angular/router";

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss']
})
export class ErrorComponent implements OnInit {

  erroCode: string;

  constructor(private route: ActivatedRoute,private router : Router) {
    this.erroCode=route.snapshot.params.code ? route.snapshot.params.code : "E_GENERICO";
  }

  ngOnInit(): void {
  }

  goHome() {
    this.router.navigateByUrl("/");
  }
}
