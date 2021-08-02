import { Component, OnDestroy, OnInit } from '@angular/core';
import { ReplaySubject } from "rxjs";
import { takeUntil, map } from "rxjs/operators";
import { SideNavService } from "../../services/side-nav.service"
import { TableService } from "../../services/table.service";
import { PlayerService } from "../../services/player.service";
import { Table } from "../../modules/api/model/models";
import { isPlayerAtTable } from "../../utils/table.utils";

@Component({
  selector: 'ecs-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit, OnDestroy {

  private destroyed$: ReplaySubject<void> = new ReplaySubject<void>(1);

  public isSideNavOpen = false;
  public table: Table | undefined = undefined;
  private playerId = "";

  constructor(
    private sideNavService: SideNavService,
    private tableService: TableService,
    private playerService: PlayerService
  ){}

  ngOnInit(): void {
    this.playerId = this.playerService.whoami;
    this.tableService.init();
    this.tableService.tables$
      .pipe(
        takeUntil(this.destroyed$),
	map((tables: Table[]) => tables.find((t: Table) => isPlayerAtTable(t, this.playerId)))
      )
      .subscribe((table: Table | undefined) => this.table = table);
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
