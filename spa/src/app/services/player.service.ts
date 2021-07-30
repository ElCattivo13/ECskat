import { Injectable } from '@angular/core';
import { PlayerApiService } from "./player.api.service";
import { Player, PlayerResponse } from "../modules/api/model/models";
import { ReplaySubject, Observable } from "rxjs";
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class PlayerService {

  private initialized = false;

  private players: ReplaySubject<Player[]> = new ReplaySubject<Player[]>(1);

  constructor(
    private apiService: PlayerApiService,
    private cookieService: CookieService
  ) {}

  public init(): void {
    if (!this.initialized) {
      this.initialized = true;
      
      // TODO only for testing purpose, needs to be removed
      this.cookieService.set("ecs-user-id", "1");
      
      this.processPlayerResponse(
        this.apiService.findAllPlayers(),
        "Error while loading players");
    }
  }

  get players$(): Observable<Player[]> {
    return this.players.asObservable();
  }
  
  get whoami(): string {
    this.cookieService.get("ecs-user-id");
  }
  
  private processPlayerResponse(response$: Observable<PlayerResponse> , errorMsg: string): void {
    response$.subscribe(
      (response: PlayerResponse) => {
        if (response.success) {
          if (response.players) {
            this.players.next(response.players);
          } else {
            this.players.next([]);
          }
        } else {
          // TODO proper error handling
          console.error(errorMsg, response);
        }
      },
      (error: any) => {
        // TODO proper error handling
        console.error(error);
      });
  }
}
