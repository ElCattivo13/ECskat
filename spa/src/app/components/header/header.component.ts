import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { faTimes } from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'ecs-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  @Output() barsClicked: EventEmitter<boolean> = new EventEmitter<boolean>();

  public sidebarOpen = false;

  public faTimes = faTimes;

  constructor() { }

  ngOnInit(): void {
  }
  
  public barsClick(open: boolean): void {
    this.barsClicked.emit(open);
    this.sidebarOpen = !this.sidebarOpen;
  }

}
