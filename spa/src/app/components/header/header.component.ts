import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'ecs-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  @Output() barsClicked: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor() { }

  ngOnInit(): void {
  }
  
  public barsClick(): void {
    this.barsClicked.emit(true);
  }

}
