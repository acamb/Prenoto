package ac.sanbernardo.prenoto.startup

import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.auth.BcryptPasswordEncoderService
import ac.sanbernardo.prenoto.model.Configurazione
import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.repositories.ConfigurazioneRepository
import ac.sanbernardo.prenoto.repositories.UserRepository
import ac.sanbernardo.prenoto.services.PrenotazioneService
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.discovery.event.ServiceReadyEvent
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
@Requires(env= Environment.DEVELOPMENT)
class TestDataLoader {

    @Inject
    UserRepository userRepository
    @Inject
    ConfigurazioneRepository configurazioneRepository
    @Inject
    PrenotazioneService prenotazioneService
    @Inject
    BcryptPasswordEncoderService encoder

    @EventListener
    @Logged
    void loadData(final ServiceReadyEvent event){
        LoggerFactory.getLogger(getClass()).info("loading test data")
        Date today = new Date()
        Calendar c = GregorianCalendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH,-1)
        Date yesterday = c.getTime()
        c.add(Calendar.DAY_OF_MONTH,2)
        Date tomorrow = c.getTime()
        userRepository.save(new User(
                username: "test",
                password: encoder.encode("aaa"),
                nome: "test",
                cognome: "test",
                active: true,
                dataFineValiditaGreenPass: tomorrow,
                dataFineVisitaAgonistica: tomorrow,
                cambioPassword: true
        ))
        userRepository.save(new User(
                username: "test1",
                password: encoder.encode("aaa"),
                nome: "test1",
                cognome: "test1",
                active: true,
                dataFineValiditaGreenPass: yesterday,
                dataFineVisitaAgonistica: tomorrow,
                cambioPassword: true
        ))
        userRepository.save(new User(
                username: "test2",
                password: encoder.encode("aaa"),
                nome: "test2",
                cognome: "test2",
                active: true,
                cambioPassword: true,
                dataFineValiditaGreenPass: tomorrow,
                dataFineVisitaAgonistica: today,
                role: User.Roles.USER.name()
        ))
        userRepository.save(new User(
                username: "test3",
                password: encoder.encode("aaa"),
                nome: "test3",
                cognome: "test3",
                active: false,
                cambioPassword: true,
                dataFineValiditaGreenPass: null ,
                dataFineVisitaAgonistica: tomorrow,
                role: User.Roles.OPERATOR.name()
        ))
        userRepository.save(new User(
                username: "test4",
                password: encoder.encode("aaa"),
                nome: "test4",
                cognome: "test4",
                active: true,
                cambioPassword: true,
                dataFineValiditaGreenPass: null,
                dataFineVisitaAgonistica: null,
                role: User.Roles.ADMIN.name()
        ))
        configurazioneRepository.save(new Configurazione(
                chiave: Configurazione.ConfigTokens.MAX_PRENOTAZIONI_UTENTE_SETTIMANA.name(),
                valore: 3,
                valido: true
        ))
        configurazioneRepository.save(new Configurazione(
                chiave: Configurazione.ConfigTokens.POSTI_PER_ORA.name(),
                valore: 3,
                valido: true
        ))
        configurazioneRepository.save(new Configurazione(
                chiave: Configurazione.ConfigTokens.ORA_INIZIO.name(),
                valore: 10,
                valido: true
        ))
        configurazioneRepository.save(new Configurazione(
                chiave: Configurazione.ConfigTokens.ORA_FINE.name(),
                valore: 20,
                valido: true
        ))
        configurazioneRepository.save(new Configurazione(
                chiave: Configurazione.ConfigTokens.NUMERO_ORE_MAX.name(),
                valore: 3,
                valido: true
        ))
        //prenotazioneService.creaSlotNuovaSettimana()
        //prenotazioneService.impostaSlotInizializzati()
    }
}
