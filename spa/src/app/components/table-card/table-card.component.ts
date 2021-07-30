import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { Table } from "../../modules/api/model/models";
import { TableService } from "../../services/table.service";
import { ReplaySubject } from "rxjs";
import { takeUntil } from "rxjs/operators";
import { SideNavService } from "../../services/side-nav.service";

@Component({
  selector: 'ecs-table-card',
  templateUrl: './table-card.component.html',
  styleUrls: ['./table-card.component.css']
})
export class TableCardComponent implements OnInit, OnDestroy {

  @Input() public table!: Table;
  @Input() public playerId!: string;
  public joinedTableId: string | undefined = "";
  private destroyed$: ReplaySubject<void> = new ReplaySubject<void>(1);

  constructor(
    private tableService: TableService,
    private sideNavService: SideNavService
  ){}

  ngOnInit(): void {
    this.tableService.joinedTable$
      .pipe(takeUntil(this.destroyed$))
      .subscribe(t => this.joinedTableId = t?.id);
  }

  ngOnDestroy(): void {
    this.destroyed$.next();
    this.destroyed$.complete();
  }

  public joinTable(id: string | undefined): void {
    if (id && (id === this.table.id)) {
      this.tableService.leaveTable();
    } else {
      this.tableService.joinTable(this.table);
      this.sideNavService.close();
    }
  }
  
  public deleteTable(tableId: string | undefined): void {
    if (tableId) {
      this.tableService.deleteTable(tableId);
    }
  }

}
