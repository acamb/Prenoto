
export interface User {
  id: number;
  nome: string;
  cognome: string;
  username: string;
  active: boolean;
  role: string;
  cambioPassword: boolean;
  dataFineValiditaGreenPass: Date;
  dataFineVisitaAgonistica: Date;
}
