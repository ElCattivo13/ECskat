import { Component, OnDestroy, OnInit } from '@angular/core';
import { ReplaySubject } from "rxjs";
import { takeUntil } from "rxjs/operators";
import { SideNavService } from "../../services/side-nav.service"
import { TableService } from "../../services/table.service";
import { Table } from "../../modules/api/model/models";

@Component({
  selector: 'ecs-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit, OnDestroy {

  private destroyed$: ReplaySubject<void> = new ReplaySubject<void>(1);

  public isSideNavOpen = false;
  public table: Table | null = null;

  constructor(private sideNavService: SideNavService, private tableService: TableService) { }

  ngOnInit(): void {
    this.tableService.init();
    this.tableService.joinedTable$
      .pipe(takeUntil(this.destroyed$))
      .subscribe(table => this.table = table);
    this.sideNavService.isSideNavOpen$
      .pipe(takeUntil(this.destroyed$))
      .subscribe(open => this.isSideNavOpen = open);
  }

  ngOnDestroy(): void {
    this.destroyed$.next();
    this.destroyed$.complete();
  }

  public sideNavOpened(): void {
    this.sideNavService.open();
  }

  public sideNavClosed(): void {
    this.sideNavService.close();
  }

}
