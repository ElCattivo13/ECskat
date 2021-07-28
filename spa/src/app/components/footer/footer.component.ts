import { Component, OnInit } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { TableService } from "../../services/table.service";
import { Table } from "../../modules/api/model/models";
import { Observable, of } from "rxjs";

@Component({
  selector: 'ecs-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {

  public lang = "";
  public joinedTable$: Observable<Table | null> = of(null);

  constructor(
    private cookieService: CookieService,
    private tableService: TableService
  ) {}

  ngOnInit(): void {
    this.lang = this.cookieService.get('ecs-language');
    this.joinedTable$ = this.tableService.joinedTable$;
  }

}
