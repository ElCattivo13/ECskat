import { Injectable } from '@angular/core';
import { Observable, of } from "rxjs";
import { DefaultService as ApiService } from "../modules/api/api/default.service";
import { Table, TableResponse } from "../modules/api/model/models";

@Injectable({
  providedIn: 'root'
})
export class TableApiService {
  
  constructor(private apiService: ApiService) { }
  
  public addTable(name: string, tables: Table[]): Observable<TableResponse> {
    //return this.apiService.ecskatTableCreateNamePost(name);
    return of({
      success: true,
      tables: [ ...tables, {name}]
    });
  }
}
