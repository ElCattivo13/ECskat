import { Injectable } from '@angular/core';
import { Observable, of } from "rxjs";
import { DefaultService as ApiService } from "../modules/api/api/default.service";
import { Player, PlayerResponse } from "../modules/api/model/models";

@Injectable({
  providedIn: 'root'
})
export class PlayerApiService {

  public players: Player[] = [
    { id: "1", name: "ElCattivo" },
    { id: "2", name: "Schorch" },
    { id: "3", name: "Kallamahusch" },
    { id: "4", name: "RainerApfelsaft" }
  ];

  constructor(private api: ApiService) { }
  
  public findAllPlayers(): Observable<PlayerResponse> {
    // TODO actaully call backend
    return of({
      success: true,
      players: this.players
    });
  }
}
