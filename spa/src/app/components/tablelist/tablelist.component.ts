import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'ecs-tablelist',
  templateUrl: './tablelist.component.html',
  styleUrls: ['./tablelist.component.css']
})
export class TablelistComponent implements OnInit {

  @Input() isOpen: boolean;

  constructor() { }

  ngOnInit(): void {
  }

}
