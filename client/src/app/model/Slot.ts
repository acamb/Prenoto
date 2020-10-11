import {User} from "./User";

export class Slot {
  id: number
  ora: number
  giorno: number
  posti: number
  visibile: boolean
  iscritti: Array<User>
  dataOraSlot: Date
}
