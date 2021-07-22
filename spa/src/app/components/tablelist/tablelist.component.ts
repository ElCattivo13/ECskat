import { ChangeDetectorRef, Component, Input, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Observable, of, ReplaySubject, Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";
import { Table } from "../../modules/api/model/models";
import { TableService } from "../../services/table.service"
import { MatDialog, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'ecs-tablelist',
  templateUrl: './tablelist.component.html',
  styleUrls: ['./tablelist.component.css']
})
export class TablelistComponent implements OnInit, OnDestroy {

  @ViewChild('addTableDialog') public addTableDialogTemplate!: TemplateRef<any>;

  @Input() isOpen = false;
  
  private destroyed$: ReplaySubject<void> = new ReplaySubject<void>(1);

  public tables$: Observable<Table[]> = of([]);
  public newTableName = "";
  public dialogData: {name?: string} = {};

  constructor(private tableService: TableService, private matDialog: MatDialog) {}

  ngOnInit(): void {
    this.tableService.init();
    this.tables$ = this.tableService.tables$;
  }

  ngOnDestroy(): void {
    this.destroyed$.next();
    this.destroyed$.complete();
  }

  public openAddTableDialog(): void {
    const addTableDialog = this.matDialog.open(this.addTableDialogTemplate);
    addTableDialog.afterClosed().subscribe(result => {
      this.newTableName = result;
    });
  }
}
