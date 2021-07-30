import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Observable, of, ReplaySubject, Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";
import { Table, Player } from "../../modules/api/model/models";
import { TableService } from "../../services/table.service";
import { PlayerService } from "../../services/player.service";
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { AddTableDialogComponent } from "../add-table-dialog/add-table-dialog.component";

@Component({
  selector: 'ecs-table-list',
  templateUrl: './table-list.component.html',
  styleUrls: ['./table-list.component.css']
})
export class TableListComponent implements OnInit, OnDestroy {

  @Input() isOpen = false;
  
  private destroyed$: ReplaySubject<void> = new ReplaySubject<void>(1);

  public tables$: Observable<Table[]> = of([]);
  public players$: Observable<Player[]> = of([]);
  public myPlayerId: string = "";

  constructor(
    private tableService: TableService,
    private playerService: PlayerService,
    private matDialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.tableService.init();
    this.playerService.init();
    this.tables$ = this.tableService.tables$;
    this.players$ = this.playerService.players$;
    this.myPlayerId = this.playerService.whoami;
  }

  ngOnDestroy(): void {
    this.destroyed$.next();
    this.destroyed$.complete();
  }

  public openAddTableDialog(): void {
    const addTableDialog = this.matDialog.open(AddTableDialogComponent);
    addTableDialog.afterClosed().subscribe(result => {
      this.tableService.addTable(result);
    });
  }
}
