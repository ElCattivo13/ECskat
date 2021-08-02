import { Table } from '../modules/api/model/models';

export function isPlayerAtTable(table: Table, playerId: string): boolean {
  return !!table.spieler?.find(p => p.id === playerId);
}
