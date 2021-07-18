import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SideNavService {

  private _isSideNavOpen: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor() { }

  get isSideNavOpen$(): Observable<boolean> {
    return this._isSideNavOpen.asObservable();
  }
  
  public open(): void {
    this._isSideNavOpen.next(true);
  }

  public close(): void {
    this._isSideNavOpen.next(false);
  }
}
