export class Configurazione {
  id: number;
  chiave: string;
  valore: string;
}

export enum ConfigTokens{
  POSTI_PER_ORA= "POSTI_PER_ORA",
  ORA_INIZIO= "ORA_INIZIO",
  ORA_FINE= "ORA_FINE",
  NUMERO_ORE_MAX= "NUMERO_ORE_MAX",
  TOKEN_FE= "TOKEN_FE",
  ORE_VALIDITA_TOKEN= "ORE_VALIDITA_TOKEN",
  MAX_PRENOTAZIONI_UTENTE_SETTIMANA= "MAX_PRENOTAZIONI_UTENTE_SETTIMANA"
}
