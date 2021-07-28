import { Injectable } from '@angular/core';
import { BehaviorSubject, ReplaySubject, Observable, of } from "rxjs";
import { TableApiService } from "./table.api.service";
import { Table, TableResponse } from "../modules/api/model/models";

@Injectable({
  providedIn: 'root'
})
export class TableService {
  
  private initialized = false;

  private tables: ReplaySubject<Table[]> = new ReplaySubject<Table[]>(1);
  private joinedTable: BehaviorSubject<Table | null> = new BehaviorSubject<Table | null>(null);

  constructor(private apiService: TableApiService) { }
  
  init() {
    if (!this.initialized) {
      this.initialized = true;
      // TODO actually call api service
      setTimeout(() => this.tables.next([
        { id: "A", creatorId: "1", name: "Table 1" },
        { id: "B", creatorId: "1", name: "Table 2" },
        { id: "C", creatorId: "2", name: "Table 3" },
        { id: "D", creatorId: "3", name: "Table 4" }
      ]), 1000);
    }
  }
  
  get tables$(): Observable<Table[]> {
    return this.tables.asObservable();
  }

  get joinedTable$(): Observable<Table | null> {
    return this.joinedTable.asObservable();
  }
  
  public addTable(tableName: string) {
    let currentTables: Table[] =[];
    this.tables.subscribe(t => currentTables = t);
    this.apiService.addTable(tableName, currentTables).subscribe(
      (response: TableResponse) => {
        if (response.success) {
          if (response.tables) {
            this.tables.next(response.tables);
          } else {
            this.tables.next([]);
          }
        } else {
          // TODO proper error handling
          console.error('Error while adding table', response);
        }
      },
      (error: any) => {
        // TODO proper error handling
        console.error(error);
      });
  }

  public joinTable(table: Table): void {
    this.joinedTable.next(table);
  }

  public leaveTable(): void {
    this.joinedTable.next(null);
  }
}
