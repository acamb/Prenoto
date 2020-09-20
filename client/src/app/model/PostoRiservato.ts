import {TipoIscrizione} from "../services/posti-riservati.service";

export class PostoRiservato{
  id: number
  giorno: number
  ora: number
  numeroOre: number
  tipoIcrizione: TipoIscrizione
  userId: number
}
