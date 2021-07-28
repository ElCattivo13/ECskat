import { Injectable } from '@angular/core';
import { PlayerApiService } from "./player.api.service";
import { Player, PlayerResponse } from "../modules/api/model/models";
import { ReplaySubject, Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PlayerService {

  private initialized = false;

  private players: ReplaySubject<Player[]> = new ReplaySubject<Player[]>(1);

  constructor(private apiService: PlayerApiService) { }

  public init(): void {
    if (!this.initialized) {
      this.initialized = true;
      //TODO acually call api service
      setTimeout(() => this.players.next([
        {id: "1", name: "ElCattivo"},
	{id: "2", name: "Schorch"},
	{id: "3", name: "Kallamahusch"},
	{id: "4", name: "RainerApfelsaft"}
      ]), 1000);
    }
  }

  get players$(): Observable<Player[]> {
    return this.players.asObservable();
  }
}
