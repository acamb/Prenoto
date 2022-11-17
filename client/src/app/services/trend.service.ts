import { Observable } from 'rxjs';
import { DailyTrend } from './../model/DailyTrend';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { getServer } from './Utils';

@Injectable({
  providedIn: 'root'
})
export class TrendService {

  constructor(private http: HttpClient) { }

  getDailyTrend(giorno: number) : Observable<DailyTrend> {
    return this.http.get<DailyTrend>(getServer()+"api/trend/day/" + giorno);
  }

}
