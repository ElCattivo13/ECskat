import { Component, OnInit } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { TranslateService } from "@ngx-translate/core";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'ecskat';

  constructor(
    private cookieService: CookieService,
    private translateService: TranslateService
  ) {}

  ngOnInit(): void {
    const lang = this.cookieService.get("ecs-language");
    if (lang) {
      this.translateService.use(lang);
    }
  }
}
