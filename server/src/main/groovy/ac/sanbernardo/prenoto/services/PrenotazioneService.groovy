package ac.sanbernardo.prenoto.services

import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.model.Prenotazione
import ac.sanbernardo.prenoto.model.SlotPrenotazione
import ac.sanbernardo.prenoto.model.TipoIscrizione

import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
@Transactional
class PrenotazioneService {

    /**
     *
     * @param user
     * @param slots lista dei singoli slot orari da prenotare
     * @return La lista delle prenotazioni salvate o null se almeno una prenotazione non e' possibile: in questo caso sono annullate tutte
     */

    List<Prenotazione> prenota(User user, List<SlotPrenotazione> slots){

    }

    /**
     * Cancella la singola prenotazione (per piu' ore chiamare per ogni prenotazione)
     * @param prenotazione
     */
    cancellaPrenotazione(Prenotazione prenotazione){

    }

    /**
     *
     * @return gli slot attualmente attivi su cui e' possibile prenotarsi (attivi ed inizializzati) se ci sono posti liberi
     */
    List<SlotPrenotazione> getSlotsAttuali(){

    }

    /**
     *
     * @param tipoIscrizione crea le prenotazioni per il tipo di lista passato
     */
    iscriviRiservati(TipoIscrizione tipoIscrizione){

    }

    /**
     *  Chiude tutti gli slot per la settimana corrente
     */
    annullaSlotCorrenti(){

    }

    /**
     * Crea gli slot per la nuova settimana
     * @return La lista dei nuovi slot
     */
    List<SlotPrenotazione> creaSlotNuovaSettimana(){

    }

    /**
     * Marca gli slot attivi come 'inizializzati', rendendoli visibili dal frontend
     */
    impostaSlotInizializzati(){

    }

    /**
     *
     * @param user
     * @return La lista di prenotazioni attive per l'utente
     */
    List<Prenotazione> getPrenotazioniPerArciere(User user){

    }


}
