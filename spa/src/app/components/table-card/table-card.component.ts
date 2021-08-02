import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { Table } from "../../modules/api/model/models";
import { TableService } from "../../services/table.service";
import { ReplaySubject } from "rxjs";
import { takeUntil, map } from "rxjs/operators";
import { SideNavService } from "../../services/side-nav.service";

@Component({
  selector: 'ecs-table-card',
  templateUrl: './table-card.component.html',
  styleUrls: ['./table-card.component.css']
})
export class TableCardComponent implements OnInit, OnDestroy {

  @Input() public table!: Table;
  @Input() public playerId!: string;
  public joinedThis = false;
  public joinedAny = false;
  private destroyed$: ReplaySubject<void> = new ReplaySubject<void>(1);

  constructor(
    private tableService: TableService,
    private sideNavService: SideNavService
  ){}

  ngOnInit(): void {
    this.tableService.tables$
      .pipe(
        takeUntil(this.destroyed$),
        map(tables => ({
          joinedThis: !!tables.find(t => t.id == this.table.id)?.spieler?.find(p => p.id == this.playerId),
          joinedAny: !!tables.flatMap(t => t.spieler).find(p => p?.id == this.playerId)
	}))
      )
      .subscribe((j: {joinedThis: boolean, joinedAny:boolean}) => {
        this.joinedThis = j.joinedThis;
        this.joinedAny = j.joinedAny;
     });
  }

  ngOnDestroy(): void {
    this.destroyed$.next();
    this.destroyed$.complete();
  }

  public joinTable(join: boolean): void {
    if (join) {
      this.tableService.joinTable(this.table.id || '', this.playerId);
      this.sideNavService.close();
    } else {
      this.tableService.leaveTable(this.table.id || '', this.playerId);
    }
  }
  
  public deleteTable(tableId: string | undefined): void {
    if (tableId) {
      this.tableService.deleteTable(tableId);
    }
  }

}
