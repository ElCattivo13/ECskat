import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'ecs-tablelist',
  templateUrl: './tablelist.component.html',
  styleUrls: ['./tablelist.component.css']
})
export class TablelistComponent implements OnInit {

  @Input() isOpen = false;;

  constructor() { }

  ngOnInit(): void {
  }

}
