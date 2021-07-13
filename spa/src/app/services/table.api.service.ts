import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { DefaultService as ApiService } from "../modules/api/api/default.service";
import { TableResponse } from "../modules/api/model/models";

@Injectable({
  providedIn: 'root'
})
export class TableApiService {
  
  constructor(private apiService: ApiService) { }
  
  public addTable(name: string): Observable<TableResponse> {
    return this.apiService.ecskatTableCreateNamePost(name);
  }
}
