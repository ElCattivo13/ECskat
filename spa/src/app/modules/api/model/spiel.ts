/**
 * ECskat API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { Action } from './action';
import { Player } from './player';
import { Game } from './game';
import { SpielResult } from './spielResult';
import { SimpleEntryPlayerInteger } from './simpleEntryPlayerInteger';
import { Stich } from './stich';
import { GameLevel } from './gameLevel';
import { Card } from './card';


export interface Spiel { 
    id?: string;
    aktuellerReizwert?: SimpleEntryPlayerInteger;
    aktuellerStich?: Stich;
    game?: Game;
    gameLevel?: GameLevel;
    hinterhand?: Player;
    kontra?: boolean;
    letzterStich?: Stich;
    mittelhand?: Player;
    naechsteAktion?: Action;
    re?: boolean;
    skat?: Array<Card>;
    sr?: SpielResult;
    sticheGespielt?: number;
    vorhand?: Player;
    watcher?: Player;
    werIstDran?: Player;
}
