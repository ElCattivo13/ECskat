import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Observable, of, ReplaySubject, Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";
import { Table } from "../../modules/api/model/models";
import { TableService } from "../../services/table.service"

@Component({
  selector: 'ecs-tablelist',
  templateUrl: './tablelist.component.html',
  styleUrls: ['./tablelist.component.css']
})
export class TablelistComponent implements OnInit, OnDestroy {

  @Input() isOpen = false;
  
  private destroyed$: ReplaySubject<void> = new ReplaySubject<void>(1);

  public tables: Table[] = [
    { name: "Table 4" },
    { name: "Table 5" },
    { name: "Table 6" }
  ];

  public dummySubject: Subject<number> = new Subject<number>();
  public dummyString: Observable<string> = of("a");

  public tables$: Observable<Table[]> = of(this.tables);

  constructor(private tableService: TableService, private cdr: ChangeDetectorRef) {
    //this.tables$ = tableService.getTables();
  }

  ngOnInit(): void {
    this.tableService.initialize();

    this.tables$ = this.tableService.tables$;
  }

  ngOnDestroy(): void {
    this.destroyed$.next();
    this.destroyed$.complete();
  }
}
