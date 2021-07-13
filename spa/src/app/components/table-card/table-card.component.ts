import { Component, Input, OnInit } from '@angular/core';
import { Table } from "../../modules/api/model/models";

@Component({
  selector: 'ecs-table-card',
  templateUrl: './table-card.component.html',
  styleUrls: ['./table-card.component.css']
})
export class TableCardComponent implements OnInit {

  @Input() table!: Table;

  constructor() { }

  ngOnInit(): void {
  }

}
