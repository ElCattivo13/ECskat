import { Component, Input, OnInit } from '@angular/core';
import { Observable } from "rxjs";
import { Table } from "../../modules/api/model/models";

@Component({
  selector: 'ecs-tablelist',
  templateUrl: './tablelist.component.html',
  styleUrls: ['./tablelist.component.css']
})
export class TablelistComponent implements OnInit {

  @Input() isOpen = false;
  
  public tables$: Observable<Table[]>;

  constructor(private tableService: TableService) { }

  ngOnInit(): void {
    this.tables$ = this.tableService.tables$
  }
}
