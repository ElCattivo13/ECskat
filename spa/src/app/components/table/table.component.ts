import { Component, OnDestroy, OnInit } from '@angular/core';
import { ReplaySubject } from "rxjs";
import { takeUntil, map } from "rxjs/operators";
import { SideNavService } from "../../services/side-nav.service"
import { TableService } from "../../services/table.service";
import { PlayerService } from "../../services/player.service";
import { Blatt, Card, Farbe, Table } from "../../modules/api/model/models";
import { isPlayerAtTable } from "../../utils/table.utils";
import { CdkDragDrop, moveItemInArray } from "@angular/cdk/drag-drop";

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

  public karten: Card[] = [
    {blatt: Blatt.Koenig, farbe: Farbe.Gruen},
    {blatt: Blatt.Sieben, farbe: Farbe.Herz},
    {blatt: Blatt.Acht, farbe: Farbe.Herz},
    {blatt: Blatt.Neun, farbe: Farbe.Herz},
    {blatt: Blatt.Zehn, farbe: Farbe.Herz},
    {blatt: Blatt.Unter, farbe: Farbe.Herz},
    {blatt: Blatt.Unter, farbe: Farbe.Schell},
    {blatt: Blatt.Ober, farbe: Farbe.Herz},
    {blatt: Blatt.Koenig, farbe: Farbe.Herz},
    {blatt: Blatt.Ass, farbe: Farbe.Herz}
  ];
  public stich: Card[] = [];

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

  public kartenlisteDrop(event: CdkDragDrop<Card[]>): void {
    moveItemInArray(this.karten, event.previousIndex, event.currentIndex);
  }

  public kartenlisteTransform(idx: number, len: number): string {
    const initialOffset = 30;
    const radius = 300;
    const alpha = 70;
    const range = 2 * (90 - alpha);
    const step1 = range / (len - 1);
    const angleInRad = (alpha + (idx * step1)) / 180 * Math.PI;
    const refSin = radius * Math.sin(alpha/180*Math.PI);
    const sin = radius * Math.sin(angleInRad);
    const offset = initialOffset + refSin - sin;

    const ampl = -20;
    const step = -2 * ampl / (len - 1);
    const angle = ampl + (idx * step);

    return "rotate(" + angle + "deg) translateY(" + offset + "px)";
  }
}
