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
      
      this.processTableResponse(
        this.apiService.findAllTables(),
        'Error while loading tables');
    }
  }
  
  get tables$(): Observable<Table[]> {
    return this.tables.asObservable();
  }

  get joinedTable$(): Observable<Table | null> {
    return this.joinedTable.asObservable();
  }
  
  public addTable(tableName: string) {
    this.processTableResponse(
      this.apiService.addTable(tableName),
      'Error while adding table');
  }
  
  public deleteTable(tableId: string) {
    this.processTableResponse(
      this.apiService.deleteTable(tableId),
      'Error while adding table');
  }
  
  private processTableResponse(response$: Observable<TableResponse>, errorMsg: string): void {
    response$.subscribe(
      (response: TableResponse) => {
        if (response.success) {
          if (response.tables) {
            this.tables.next(response.tables);
          } else {
            this.tables.next([]);
          }
        } else {
          // TODO proper error handling
          console.error(errorMsg, response);
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
