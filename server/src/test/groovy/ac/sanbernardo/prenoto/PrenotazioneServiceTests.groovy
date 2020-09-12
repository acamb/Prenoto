package ac.sanbernardo.prenoto

import ac.sanbernardo.prenoto.exceptions.PostiEsauritiException
import ac.sanbernardo.prenoto.model.Configurazione
import ac.sanbernardo.prenoto.model.PostoRiservato
import ac.sanbernardo.prenoto.model.SlotPrenotazione
import ac.sanbernardo.prenoto.model.TipoIscrizione
import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.repositories.ConfigurazioneRepository
import ac.sanbernardo.prenoto.repositories.PostoRiservatoRepository
import ac.sanbernardo.prenoto.repositories.PrenotazioneRepository
import ac.sanbernardo.prenoto.repositories.UserRepository
import ac.sanbernardo.prenoto.services.PrenotazioneService
import io.micronaut.test.annotation.MicronautTest
import org.slf4j.LoggerFactory
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Inject

@MicronautTest(application = Application.class,packages = "ac.sanbernardo.prenoto")
class PrenotazioneServiceTests extends Specification {

    static final int POSTI_PER_ORA = 3
    static final int ORA_INIZIO = 10
    static final int ORA_FINE = 13

    @Inject
    PrenotazioneService prenotazioneService
    @Inject
    ConfigurazioneRepository configurazioneRepository
    @Inject
    UserRepository userRepository
    @Inject
    PrenotazioneRepository prenotazioneRepository
    @Inject
    PostoRiservatoRepository postoRiservatoRepository

    void initConfig(int postiPerOre = POSTI_PER_ORA){
        def configs = [
                (Configurazione.ConfigTokens.POSTI_PER_ORA.name()) : postiPerOre,
                (Configurazione.ConfigTokens.ORA_INIZIO.name()) : ORA_INIZIO,
                (Configurazione.ConfigTokens.ORA_FINE.name()) : ORA_FINE,
                (Configurazione.ConfigTokens.NUMERO_ORE_MAX.name()): 3
        ]
        configs.each { key,value ->
            configurazioneRepository.save(new Configurazione(chiave: key,valore: value))
        }
    }

    void creaSettimana(){
        prenotazioneService.creaSlotNuovaSettimana()
        prenotazioneService.impostaSlotInizializzati()
    }

    User creaUtente(String suffix){
        userRepository.save(new User(
                nome: "user_${suffix}",
                cognome: "cognome_${suffix}",
                password: "notSoSecure"
        ))
    }

    @Unroll
    void "init config test"(){
        when:
            initConfig()
        then:
            configurazioneRepository.count() == 4
    }

    @Unroll
    void "creazione settimana"(){
        when:
            initConfig()
            creaSettimana()
        then:
            prenotazioneService.getSlotsAttuali().size() == 7 * (ORA_FINE-ORA_INIZIO + 1)
    }

    @Unroll
    void "iscrizione"(){
        when:
            initConfig()
            creaSettimana()
            (1..3).each {
                prenotazioneService.prenota(creaUtente(it.toString()),
                    [new SlotPrenotazione(ora: 10,giornoSettimana: 1)]
                )
            }
        then:
            prenotazioneRepository.count() == 3
    }

    @Unroll
    void "posti finiti"(){
        when:
        initConfig()
        creaSettimana()
        (1..4).each {
            prenotazioneService.prenota(creaUtente(it.toString()),
                    [new SlotPrenotazione(ora: 10,giornoSettimana: 1)]
            )
        }
        then:
        thrown(PostiEsauritiException.class)
    }

    @Unroll
    void "iscrivi posti d'ufficio"(){
        when:
            initConfig()
            prenotazioneService.creaSlotNuovaSettimana()
            (1..nPosti).each{
                User user = creaUtente(it)
                PostoRiservato r = postoRiservatoRepository.save(new PostoRiservato(
                        giorno: 1,
                        ora: 10,
                        numeroOre: nOre,
                        userId: user.id,
                        tipoIscrizione: TipoIscrizione.UFFICIO
                ))
            }
            prenotazioneService.iscriviRiservati(TipoIscrizione.UFFICIO)
        then:
            prenotazioneRepository.count() == totale
        where:
        nPosti | nOre | totale
           3   |   2  |  6
           3   |   1  |  3
           12  |   2  | 24
           2   |   4  |  8
    }

    @Unroll
    void "iscrivi posti preferenza"(){
        when:
        initConfig(3)
        prenotazioneService.creaSlotNuovaSettimana()
        (1..nPostiRiservati).each{
            User user = creaUtente("U_${it}")
            PostoRiservato r = postoRiservatoRepository.save(new PostoRiservato(
                    giorno: 1,
                    ora: 10,
                    numeroOre: nOreRiservati,
                    userId: user.id,
                    tipoIscrizione: TipoIscrizione.UFFICIO
            ))
        }
        prenotazioneService.iscriviRiservati(TipoIscrizione.UFFICIO)
        (1..nPostiPreferenza).each{
            User user = creaUtente("P_${it}")
            PostoRiservato r = postoRiservatoRepository.save(new PostoRiservato(
                    giorno: 1,
                    ora: 10,
                    numeroOre: nOrePreferenza,
                    userId: user.id,
                    tipoIscrizione: TipoIscrizione.PREFERENZA
            ))
        }
        prenotazioneService.iscriviRiservati(TipoIscrizione.PREFERENZA)
        then:
        prenotazioneRepository.count() == totale
        where:
        nPostiRiservati | nOreRiservati | nPostiPreferenza | nOrePreferenza | totale
            3           |     1         |       4          |       1        |  3
            1           |     2         |       3          |       1        |  4
            1           |     1         |       3          |       1        |  3
            1           |     3         |       3          |       2        |  7
    }


}
