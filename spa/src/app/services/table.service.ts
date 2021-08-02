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

  constructor(private apiService: TableApiService) { }
  
  public init(): void {
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
  
  public addTable(tableName: string): void {
    this.processTableResponse(
      this.apiService.addTable(tableName),
      'Error while adding table');
  }
  
  public deleteTable(tableId: string): void {
    this.processTableResponse(
      this.apiService.deleteTable(tableId),
      'Error while adding table');
  }
  
  public joinTable(tableId: string, playerId: string): void {
    this.processTableResponse(
      this.apiService.joinTable(tableId, playerId),
      'Error while joining table');
  }
  
  public leaveTable(tableId: string, playerId: string): void {
    this.processTableResponse(
      this.apiService.leaveTable(table, playerId),
      'Error while leaving table');
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
}
