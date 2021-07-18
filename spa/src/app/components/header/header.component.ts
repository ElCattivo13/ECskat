import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { faTimes } from "@fortawesome/free-solid-svg-icons";
import { ReplaySubject } from "rxjs";
import { takeUntil } from "rxjs/operators";
import { SideNavService } from "../../services/side-nav.service";

@Component({
  selector: 'ecs-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {

  public isOpen = false;
  private destroyed$: ReplaySubject<void> = new ReplaySubject<void>(1);
  
  public faTimes = faTimes;

  constructor(private sideNavService: SideNavService) { }

  ngOnInit(): void {
    this.sideNavService.isSideNavOpen$
      .pipe(takeUntil(this.destroyed$))
      .subscribe(open => this.isOpen = open);
  }
  
  ngOnDestroy(): void {
    this.destroyed$.next();
    this.destroyed$.complete();
  }

  public barsClick(open: boolean): void {
    if (open) {
      this.sideNavService.open();
    } else {
      this.sideNavService.close();
    }
  }

  public openPreferences(): void {
    
  }

}
