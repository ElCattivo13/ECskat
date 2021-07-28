import { Component, Input, OnInit } from '@angular/core';
import { Player } from "../../modules/api/model/models";

@Component({
  selector: 'ecs-player-card',
  templateUrl: './player-card.component.html',
  styleUrls: ['./player-card.component.css']
})
export class PlayerCardComponent implements OnInit {

  @Input() public player: Player = {};

  constructor() { }

  ngOnInit(): void {
  }

}
