package ac.sanbernardo.prenoto.startup

import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.auth.BcryptPasswordEncoderService
import ac.sanbernardo.prenoto.auth.PasswordEncoder
import ac.sanbernardo.prenoto.model.Configurazione
import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.repositories.ConfigurazioneRepository
import ac.sanbernardo.prenoto.repositories.UserRepository
import ac.sanbernardo.prenoto.services.PrenotazioneService
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.runtime.event.annotation.EventListener
import io.micronaut.discovery.event.ServiceReadyEvent
import jdk.nashorn.internal.runtime.logging.Loggable

import javax.inject.Inject
import javax.inject.Singleton

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
        userRepository.save(new User(
                username: "test",
                password: encoder.encode("aaa"),
                nome: "test",
                cognome: "test",
                active: true,
                cambioPassword: true
        ))
        userRepository.save(new User(
                username: "test1",
                password: encoder.encode("aaa"),
                nome: "test1",
                cognome: "test1",
                active: true,
                cambioPassword: true
        ))
        userRepository.save(new User(
                username: "test2",
                password: encoder.encode("aaa"),
                nome: "test2",
                cognome: "test2",
                active: true,
                cambioPassword: true,
                role: User.Roles.USER.name()
        ))
        userRepository.save(new User(
                username: "test3",
                password: encoder.encode("aaa"),
                nome: "test3",
                cognome: "test3",
                active: true,
                cambioPassword: true,
                role: User.Roles.OPERATOR.name()
        ))
        userRepository.save(new User(
                username: "test4",
                password: encoder.encode("aaa"),
                nome: "test4",
                cognome: "test4",
                active: true,
                cambioPassword: true,
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
                valore: 18,
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
