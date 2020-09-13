package ac.sanbernardo.prenoto.services

import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.exceptions.NumeroOreException
import ac.sanbernardo.prenoto.exceptions.PostiEsauritiException
import ac.sanbernardo.prenoto.model.Configurazione
import ac.sanbernardo.prenoto.model.PostoRiservato
import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.model.Prenotazione
import ac.sanbernardo.prenoto.model.SlotPrenotazione
import ac.sanbernardo.prenoto.model.TipoIscrizione
import ac.sanbernardo.prenoto.repositories.ConfigurazioneRepository
import ac.sanbernardo.prenoto.repositories.PrenotazioneRepository
import ac.sanbernardo.prenoto.repositories.SlotPrenotazioneRepository
import groovy.time.TimeCategory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
@Transactional
class PrenotazioneService {

    @Inject
    SlotPrenotazioneRepository slotPrenotazioneRepository
    @Inject
    PrenotazioneRepository prenotazioneRepository
    @Inject
    PostoRiservatoService postoRiservatoService
    @Inject
    ConfigurazioneRepository configurazioneRepository

    /**
     *
     * @param user
     * @param slots lista dei singoli slot orari da prenotare
     * @return La lista delle prenotazioni salvate o lancia un eccezione se almeno una prenotazione non e' possibile: in questo caso sono annullate tutte
     */
    @Logged
    List<Prenotazione> prenota(User user, SlotPrenotazione slotPartenza,int ore) throws PostiEsauritiException, NumeroOreException{
        List<SlotPrenotazione> listaLock = []
        List<Prenotazione> prenotazioni = []
        if(ore > 3){
            throw new NumeroOreException();
        }
        listaLock = getSlotsCollegati(slotPartenza,ore)
        if(listaLock.any{ it.postiRimanenti < 1 }){
            throw new PostiEsauritiException()
        }
        listaLock.each { SlotPrenotazione slot ->
            prenotazioni << iscrivi(slot,user.id)
        }
        prenotazioni
    }

    /**
     * Cancella la singola prenotazione (per piu' ore chiamare per ogni prenotazione)
     * @param prenotazione
     */
    @Logged
    void cancellaPrenotazione(Prenotazione prenotazione){
        Prenotazione prenotazioneDb = prenotazioneRepository.findById(prenotazione.id)
        if(!prenotazioneDb || prenotazioneDb.userId != prenotazione.userId){
            throw new RuntimeException("La prenotazione non esiste")
        }
        prenotazioneRepository.delete(prenotazioneDb)
    }

    /**
     *
     * @return gli slot attualmente attivi su cui e' possibile prenotarsi (attivi ed inizializzati) se ci sono posti liberi
     */
    @Logged
    List<SlotPrenotazione> getSlotsAttuali(){
        slotPrenotazioneRepository.findByActiveTrueAndInitializedTrue()
    }

    /**
     *
     * @param tipoIscrizione crea le prenotazioni per il tipo di lista passato
     */
    @Logged
    void iscriviRiservati(TipoIscrizione tipoIscrizione){
        List<PostoRiservato> postiRiservati = postoRiservatoService.getPostiPerTipo(tipoIscrizione)
        postiRiservati.each { posto ->
            SlotPrenotazione slotPartenza = slotPrenotazioneRepository.findByGiornoSettimanaAndOra(posto.giorno,posto.ora)
            getSlotsCollegati(slotPartenza,posto.numeroOre).each{ slot ->
                if (slot.postiRimanenti > 0 || tipoIscrizione == TipoIscrizione.UFFICIO) {
                    iscrivi(slot, posto.userId)
                }
            }
        }
    }

    List<SlotPrenotazione> getSlotsCollegati(SlotPrenotazione slotPartenza,int numeroOre){
        List<SlotPrenotazione> slots = []
        (0..numeroOre-1).each { ora ->
            slots << slotPrenotazioneRepository.findByGiornoSettimanaAndOra(slotPartenza.giornoSettimana, (slotPartenza.ora+ora))
        }
        slots
    }

    /**
     *  Chiude tutti gli slot per la settimana corrente
     */
    @Logged
    void annullaSlotCorrenti(){
        slotPrenotazioneRepository.chiudiSlotAttivi()
    }

    /**
     * Crea gli slot per la nuova settimana
     * @return La lista dei nuovi slot
     */
    @Logged
    List<SlotPrenotazione> creaSlotNuovaSettimana(){
        List<SlotPrenotazione> slots = []
        int oraInizio = configurazioneRepository.findByChiaveAndValidoTrue(Configurazione.ConfigTokens.ORA_INIZIO.name()).valore.toInteger()
        int oraFine = configurazioneRepository.findByChiaveAndValidoTrue(Configurazione.ConfigTokens.ORA_FINE.name()).valore.toInteger()
        int posti = configurazioneRepository.findByChiaveAndValidoTrue(Configurazione.ConfigTokens.POSTI_PER_ORA.name()).valore.toInteger()
        (0..6).each { giorno ->
            Date data = new Date()
            use(TimeCategory){
                data = data + giorno.days
            }
            (oraInizio..oraFine).each { ora ->
                SlotPrenotazione slot = new SlotPrenotazione(
                        giornoSettimana: giorno,
                        ora: ora,
                        data: data,
                        postiRimanenti: posti
                )
                slots << slotPrenotazioneRepository.save(slot)
            }
        }
        slots
    }

    /**
     * Marca gli slot attivi come 'inizializzati', rendendoli visibili dal frontend
     */
    @Logged
    void impostaSlotInizializzati(){
        slotPrenotazioneRepository.inizializzaSlotAttivi()
    }

    /**
     *
     * @param user
     * @return La lista di prenotazioni attive per l'utente
     */
    @Logged
    List<Prenotazione> getPrenotazioniPerArciere(User user){
        prenotazioneRepository.prenotazioniAttive(user.id)
    }

    /**
     * Iscrive l'user allo slot scelto
     * @param slot
     * @param user
     * @return
     */
    @Logged
    Prenotazione iscrivi(SlotPrenotazione slot,Long user){
        Prenotazione prenotazione = new Prenotazione(
                slotPrenotazione: slot,
                userId: user
        )
        prenotazione = prenotazioneRepository.save(prenotazione)
        slot.postiRimanenti--
        slotPrenotazioneRepository.save(slot)
        prenotazione
    }


}
