import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest
} from '@angular/common/http';
import {Observable} from "rxjs";

@Injectable()
export class JwtInterceptorService implements HttpInterceptor{

  constructor() { }

  intercept(req: HttpRequest<any>, next: HttpHandler):
  Observable<HttpEvent<any>> {
    //non uso l'authenticationService per evitare dipendenze circolari (gestisce anche la chiamata all'autenticazione con l'httpClient)
    if (sessionStorage.getItem("token") != undefined) {
      req = req.clone({
        setHeaders: {
          Authorization: sessionStorage.getItem("token")
        }
      })
    }
    return next.handle(req);
  }

}
