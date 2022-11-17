import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent implements OnInit {

  @Input()
  isVisible = false
  @Output()
  onClose = new EventEmitter<null>();

  get modalClass(){
    return this.isVisible ? "show" : ""
  }

  get modalDisplayStyle(){
    return this.isVisible ? "block" : "none"
  }

  constructor() { }

  ngOnInit(): void {
  }

  close(){
    this.onClose.emit();
  }

}
