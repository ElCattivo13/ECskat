import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { TableComponent } from './components/table/table.component';
import { TablelistComponent } from './components/tablelist/tablelist.component';
import { StichComponent } from './components/stich/stich.component';

@NgModule({
  declarations: [
	  AppComponent, 
	  StichComponent,
	  FooterComponent,
	  HeaderComponent,
	  TableComponent,
	  TablelistComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
