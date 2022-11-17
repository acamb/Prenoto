import {environment} from "../../environments/environment";

export function extractData(res){
    return res.json() || {};
  }

 export function getServer(): string{
   if ( environment.ambiente === 'local' ){
     return '//localhost:8080/';
   }
   else {
     const getUrl = window.location;
     //const baseUrl = getUrl.protocol + '//' + getUrl.host + environment.context
     const baseUrl = '/';
     return baseUrl;
   }
  }

  export function getGiornoFromNumero(giorno: number): string {
      switch(giorno){
        case 0: return "Lunedi'";
        case 1: return "Martedi'";
        case 2: return "Mercoledi'";
        case 3: return "Giovedi'";
        case 4: return "Venerdi'";
        case 5: return "Sabato";
        case 6: return "Domenica";
        default: return null;
      }
  }

  export const range = (start, end) => Array.from(Array(end - start + 1).keys()).map(x => x + start);