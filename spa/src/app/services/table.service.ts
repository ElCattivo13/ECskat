import { Injectable , OnInit } from '@angular/core';
import { BehaviorSubject, Observable } from "rxjs";
import { TableApiService } from "./table.api.service";
import { Table, TableResponse } from "../modules/api/model/models";

@Injectable({
  providedIn: 'root'
})
export class TableService implements OnInit {

  private tables: BehaviorSubject<Table[]> = new BehaviorSubject([]);

  constructor(private apiService: TableApiService) { }
  
  ngOnInit() {
    // TODO actually call api service
    this.tables.next([
      { name: "Table 1" },
      { name: "Table 2" },
      { name: "Table 3" },
      { name: "Table 4" }
    ]);
  }
  
  get tables$(): Observable<Table[]> {
    return tables.asObservable();
  }
  
  public addTable(tableName: string) {
    this.apiService.addTable(tableName).subscribe(
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
      !  // TODO proper error handling
        console.error(error);
      });
  }
}
