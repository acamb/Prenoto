package ac.sanbernardo.prenoto.services

import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.exceptions.MaxNumeroIscrizioniSuperateException
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
import javax.persistence.NoResultException
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
    List<Prenotazione> prenota(User user, SlotPrenotazione slotPartenza,int ore) throws PostiEsauritiException, NumeroOreException,MaxNumeroIscrizioniSuperateException{
        List<SlotPrenotazione> listaLock = []
        List<Prenotazione> prenotazioni = []
        if(ore > 3){
            throw new NumeroOreException();
        }
        SlotPrenotazione slotDb = slotPrenotazioneRepository.findById(slotPartenza.id).get()
        listaLock = getSlotsCollegati(slotDb,ore)
        if(listaLock.any{ it.postiRimanenti < 1 }){
            throw new PostiEsauritiException()
        }
        listaLock.each { SlotPrenotazione slot ->
            if(verificaIscrivibilita(slot,TipoIscrizione.UTENTE,user.id)) {
                prenotazioni << iscrivi(slot, user.id)
            }
            else{
                throw new MaxNumeroIscrizioniSuperateException()
            }
        }
        prenotazioni
    }

    /**
     * Cancella la singola prenotazione (per piu' ore chiamare per ogni prenotazione)
     * @param prenotazione
     */
    @Logged
    void cancellaPrenotazione(User user,Long id){
        Prenotazione prenotazioneDb = prenotazioneRepository.findById(id).get()
        SlotPrenotazione slot = prenotazioneDb.slotPrenotazione
        if(!prenotazioneDb || prenotazioneDb.userId != user.id){
            throw new RuntimeException("La prenotazione non esiste")
        }
        slot.postiRimanenti++
        prenotazioneRepository.delete(prenotazioneDb)
        slotPrenotazioneRepository.save(slot)
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
                if (verificaIscrivibilita(slot,tipoIscrizione,posto.userId)) {
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
            Date data = new Date() //TODO[AC] deve essere l'inizio settimana
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
    List<Prenotazione> getPrenotazioniPerArciere(Long userId){
        try {
            prenotazioneRepository.prenotazioniAttive(userId)
        }
        catch(NoResultException ex){
            return []
        }
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
        if(slot.postiRimanenti < 0){
            slot.postiRimanenti = 0
        }
        slotPrenotazioneRepository.save(slot)
        prenotazione
    }


    boolean verificaIscrivibilita(SlotPrenotazione slotPrenotazione, TipoIscrizione tipoIscrizione,Long userId) {
        if(tipoIscrizione == TipoIscrizione.UFFICIO){
            return true
        }
        if(slotPrenotazione.postiRimanenti < 1){
            return false
        }

        int maxGiorni = configurazioneRepository.findByChiaveAndValidoTrue(Configurazione.ConfigTokens.MAX_PRENOTAZIONI_UTENTE_SETTIMANA.name()).valore.toInteger()
        int maxOre = configurazioneRepository.findByChiaveAndValidoTrue(Configurazione.ConfigTokens.NUMERO_ORE_MAX.name()).valore.toInteger()

        Prenotazione[] prenotazioniFatte = getPrenotazioniPerArciere(userId)
        Prenotazione[] adiacenti = prenotazioniFatte.findAll{it.slotPrenotazione.giornoSettimana == slotPrenotazione.giornoSettimana}
        //Se ci sono altre prenotazioni lo stesso giorno devo controllare che siano adiacenti e max ore
        if(adiacenti){
            if(adiacenti.size() >= maxOre){
                return false
            }
            List<Integer> ore = adiacenti.collect{it.slotPrenotazione.ora}
            ore << slotPrenotazione.ora
            ore.sort()
            //controllo che siano consecutive
            for(int i = 0;i<ore.size()-1;i++){
                if(!(ore[i]+1).equals(ore[i+1])){
                    return false
                }
            }

        }
        else {
            int giorniPrenotati = getPrenotazioniPerArciere(userId).groupBy { it.slotPrenotazione.giornoSettimana }.keySet().size()
            if (giorniPrenotati >= maxGiorni) {
                return false
            }
        }

        return true
    }

    def void nuovaSettimana() {
        annullaSlotCorrenti()
        creaSlotNuovaSettimana()
        iscriviRiservati(TipoIscrizione.UFFICIO)
        iscriviRiservati(TipoIscrizione.PREFERENZA)
        impostaSlotInizializzati()
    }
}
