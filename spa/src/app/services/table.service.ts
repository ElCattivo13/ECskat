import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TableService {

  private tables: BehaviorSubject<Table[]> = new BehaviorSubject([]);

  constructor(private apiService: TableApiService) { }
  
  public tables$(): Observable<Table[]> {
      return tables.asObservable();
  }
  
  public addTable(table: Table) {
      this.apiService.addTable(table).subscribe(
          (response) => {
              if (response.success) {
                  if (response.tables) {
                      tables.next(response.tables);
                  } else {
                      tables.next([]);
                  }
              } else {
                  // TODO proper error handling
                  console.error('Error while adding table', response);
              }
          },
          (error) => {
              // TODO proper error handling
              console.error(error);
          });
  }
}
