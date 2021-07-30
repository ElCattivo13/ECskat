import { Injectable } from '@angular/core';
import { Observable, of } from "rxjs";
import { DefaultService as ApiService } from "../modules/api/api/default.service";
import { Table, TableResponse } from "../modules/api/model/models";

@Injectable({
  providedIn: 'root'
})
export class TableApiService {
  
  private tables: Table[] = [
    { id: "A", creatorId: "1", name: "Table 1" },
    { id: "B", creatorId: "1", name: "Table 2" },
    { id: "C", creatorId: "2", name: "Table 3" },
    { id: "D", creatorId: "3", name: "Table 4" }
  ];
  private i = 0;
  
  constructor(private apiService: ApiService) { }
  
  get tableResponse(): Observable<TableResponse> {
     return of({
       success: true,
       tables: this.tables
     });
   }
  
  public findAllTables(): Observable<TableResponse> {
    // TODO actually call backend service
    return this.tableResponse;
  }
  
  public addTable(name: string): Observable<TableResponse> {
    //return this.apiService.ecskatTableCreateNamePost(name);
    this.i++;
    this.tables.push({id: "E"+this.i, creatorId: "1", name})
    return this.tableResponse;
  }
  
  public deleteTable(tableId: string): Observable<TableResponse> {
    const i = this.tables.findIndex(t => t.id == tableId);
    if (i >= 0) {
      this.tables.splice(i,1);
    }
    return this.tableResponse;;
  }
}
