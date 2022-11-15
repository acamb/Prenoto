import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-slot-free-space',
  templateUrl: './slot-free-space.component.html',
  styleUrls: ['./slot-free-space.component.scss']
})
export class SlotFreeSpaceComponent implements OnInit {

  @Input()
  amount: number=0;
  @Input()
  total: number=1;
  @Input()
  firstThreshold=0.4;
  @Input()
  secondThreshold=0.7;
  @Input()
  showAmount=false;

  get color(){
    if(this.amount == 0){
      return "linear-grey"
    }
    if(this.amount/this.total <= this.firstThreshold){
      return "linear-green"
    }
    else if(this.amount/this.total <= this.secondThreshold){
      return "linear-yellow";
    }
    else{
      return "linear-red";
    }
  }

  constructor() { }

  ngOnInit(): void {
  }

}
