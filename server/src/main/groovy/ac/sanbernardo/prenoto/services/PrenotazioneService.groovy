package ac.sanbernardo.prenoto.services

import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.exceptions.IscrizioneNelPassatoNonCancellabileException
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
import jakarta.inject.Inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.persistence.NoResultException
import javax.transaction.Transactional
import jakarta.inject.Singleton

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

    Logger log = LoggerFactory.getLogger(PrenotazioneService.class);

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
        int oreMax = configurazioneRepository.findByChiaveAndValidoTrue(Configurazione.ConfigTokens.NUMERO_ORE_MAX.name()).orElse(null)?.valore?.toInteger() ?: 3
        if(ore > oreMax){
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
        List<Prenotazione> prenotazioniCollegate = getPrenotazioniPerArciere(user.id)
                .findAll{
                    it.slotPrenotazione.giornoSettimana == prenotazioneDb.slotPrenotazione.giornoSettimana
                }
                .sort{it.slotPrenotazione.ora}

        int idx = prenotazioniCollegate.indexOf(prenotazioneDb)
        if(idx != 0 && idx != prenotazioniCollegate.size()-1){
            throw new RuntimeException("Impossibile cancellare la prenotazione")
        }
        use(TimeCategory) {
            Calendar c = new GregorianCalendar()
            c.setTime(prenotazioneDb.slotPrenotazione.data)
            c.set(Calendar.HOUR_OF_DAY,prenotazioneDb.slotPrenotazione.ora)
            if(c.getTime() < new Date()){
                throw new IscrizioneNelPassatoNonCancellabileException()
            }
        }
        slot.postiRimanenti++
        prenotazioneRepository.delete(prenotazioneDb)
        slotPrenotazioneRepository.save(slot)
    }

    /**
     *
     * @return gli slot attualmente attivi su cui e' possibile prenotarsi (attivi ed inizializzati) se ci sono posti liberi
     */
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

    @Logged
    List<SlotPrenotazione> getSlotsCollegati(SlotPrenotazione slotPartenza,int numeroOre){
        List<SlotPrenotazione> slots = []
        (0..numeroOre-1).each { ora ->
            try {
                slots << slotPrenotazioneRepository.findByGiornoSettimanaAndOra(slotPartenza.giornoSettimana, (slotPartenza.ora + ora))
            }
            catch(all){
                log.warn("richiesto slot collegato che non esiste: " + slotPartenza.toString() + ", ora: ${ora}")
            }
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
        int oraInizio = configurazioneRepository.findByChiaveAndValidoTrue(Configurazione.ConfigTokens.ORA_INIZIO.name()).orElseThrow().valore.toInteger()
        int oraFine = configurazioneRepository.findByChiaveAndValidoTrue(Configurazione.ConfigTokens.ORA_FINE.name()).orElseThrow().valore.toInteger()
        int posti = configurazioneRepository.findByChiaveAndValidoTrue(Configurazione.ConfigTokens.POSTI_PER_ORA.name()).orElseThrow().valore.toInteger()
        (0..6).each { giorno ->
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, configurazioneRepository.findByChiaveAndValidoTrue(Configurazione.ConfigTokens.ORA_FINE.name()).orElseThrow().valore.toInteger()); // ! clear would not reset the hour of day !
            cal.clear(Calendar.MINUTE)
            cal.clear(Calendar.SECOND)
            cal.clear(Calendar.MILLISECOND);

            cal.set(Calendar.DAY_OF_WEEK, 2); //lunedi'
            Date data = cal.getTime()
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

    @Logged
    boolean verificaIscrivibilita(SlotPrenotazione slotPrenotazione, TipoIscrizione tipoIscrizione,Long userId) {
        if(tipoIscrizione == TipoIscrizione.UFFICIO){
            return true
        }
        if(slotPrenotazione.postiRimanenti < 1){
            log.info("prenotazione scartata per posti insufficienti")
            return false
        }
        if(slotPrenotazione.getDataOraSlot().before(new Date())){
            log.info("prenotazione scartata perche' nel passato")
            return false
        }


        int maxGiorni = configurazioneRepository.findByChiaveAndValidoTrue(Configurazione.ConfigTokens.MAX_PRENOTAZIONI_UTENTE_SETTIMANA.name()).orElseThrow().valore.toInteger()
        int maxOre = configurazioneRepository.findByChiaveAndValidoTrue(Configurazione.ConfigTokens.NUMERO_ORE_MAX.name()).orElseThrow().valore.toInteger()

        Prenotazione[] prenotazioniFatte = getPrenotazioniPerArciere(userId)
        Prenotazione[] adiacenti = prenotazioniFatte.findAll{it.slotPrenotazione.giornoSettimana == slotPrenotazione.giornoSettimana}
        //Se ci sono altre prenotazioni lo stesso giorno devo controllare che siano adiacenti e max ore
        if(adiacenti){
            if(adiacenti.size() >= maxOre){
                log.info("prenotazione scartata per posti adiacenti >= maxOre")
                return false
            }
            List<Integer> ore = adiacenti.collect{it.slotPrenotazione.ora}
            ore << slotPrenotazione.ora
            ore.sort()
            //controllo che siano consecutive
            for(int i = 0;i<ore.size()-1;i++){
                if(!(ore[i]+1).equals(ore[i+1])){
                    log.info("prenotazione scartata per posti non adiacenti")
                    return false
                }
            }

        }
        else {
            int giorniPrenotati = getPrenotazioniPerArciere(userId).groupBy { it.slotPrenotazione.giornoSettimana }.keySet().size()
            if (giorniPrenotati >= maxGiorni) {
                log.info("prenotazione scartata per giorni max superati")
                return false
            }
        }

        return true
    }

    @Logged
    def void nuovaSettimana() {
        annullaSlotCorrenti()
        creaSlotNuovaSettimana()
        iscriviRiservati(TipoIscrizione.UFFICIO)
        iscriviRiservati(TipoIscrizione.PREFERENZA)
        impostaSlotInizializzati()
    }

    List<User> getUtentiIscritti(Long slotId){
        slotPrenotazioneRepository.getUtentiIscritti(slotId)
    }
}
