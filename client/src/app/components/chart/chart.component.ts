import { AppStateService } from './../../services/app-state.service';
import { Configurazione } from './../../model/Configurazione';
import { JsonPipe } from '@angular/common';
import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss']
})
export class ChartComponent implements OnInit,OnChanges {

  chart: any;
  @Input()
  data: Array<number>;
  @Input()
  labels: string[];
  @Input()
  isVisible = false;
  @Input()
  oreMax = 10;

  lastVisibleStatus = false;

  constructor() { 
  }

  ngOnInit(): void {
    
  }

ngOnChanges(changes: SimpleChanges): void {
  if(this.isVisible && !this.lastVisibleStatus && this.data && this.oreMax){
    this.createChart();
    this.lastVisibleStatus = true;
  }
  else if(!this.isVisible && this.lastVisibleStatus){
    this.lastVisibleStatus = false;
    this.closeChart();
  }
}

  createChart(){
    console.log("credo chart,ore: " + this.oreMax)
    this.chart = new Chart("chart",{
      type: 'line',
      data: {
        labels: this.labels,

        datasets: [{
          label: "Iscrizioni",
          data: this.data,
          fill: 'start',
          tension: 0.3,
          borderColor:"#FF0000"
        }]
      },
      options:{
        scales: {
          x:{
              title: {
                display: true,
                text: "Ore"
              }
          },
          y:{
            beginAtZero: true,
            min: 0,
            max: this.oreMax, 
            ticks:{
              precision: 0,
              //la callback e' necessaria altrimenti su screen piccoli mostra l'ultimo valore sulla y con molti 0 (?!)
              callback: (yValue: number) => {
                return Math.floor(yValue); // format to your liking
              }
            }
        }
        }
      }

    });
  }

  closeChart(){
    this.chart && this.chart.destroy();
  }

}
