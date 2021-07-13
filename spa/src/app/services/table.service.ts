import { Injectable } from '@angular/core';
import { BehaviorSubject, ReplaySubject, Observable, of } from "rxjs";
import { TableApiService } from "./table.api.service";
import { Table, TableResponse } from "../modules/api/model/models";

@Injectable({
  providedIn: 'root'
})
export class TableService {
  
  private initialized = false;

  public tables: ReplaySubject<Table[]> = new ReplaySubject<Table[]>(1);

  constructor(/*private apiService: TableApiService*/) { }
  
  initialize() {
    if (!this.initialized) {
      this.initialized = true;
      // TODO actually call api service
      setTimeout(() => this.tables.next([
        { name: "Table 1" },
        { name: "Table 2" },
        { name: "Table 3" },
        { name: "Table 4" }
      ]), 1000);
    }
  }
  
  get tables$(): Observable<Table[]> {
    return this.tables.asObservable();
  }
  
  public addTable(tableName: string) {
//    this.apiService.addTable(tableName).subscribe(
//      (response: TableResponse) => {
//        if (response.success) {
//          if (response.tables) {
//            this.tables.next(response.tables);
//          } else {
//            this.tables.next([]);
//          }
//        } else {
//          // TODO proper error handling
//          console.error('Error while adding table', response);
//        }
//      },
//      (error: any) => {
//        // TODO proper error handling
//        console.error(error);
//      });
  }
}
