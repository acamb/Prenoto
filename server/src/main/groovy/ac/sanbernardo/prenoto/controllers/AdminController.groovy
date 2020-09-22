package ac.sanbernardo.prenoto.controllers

import ac.sanbernardo.prenoto.Utils
import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.model.Configurazione
import ac.sanbernardo.prenoto.model.TipoIscrizione
import ac.sanbernardo.prenoto.repositories.ConfigurazioneRepository
import ac.sanbernardo.prenoto.services.PrenotazioneService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import org.slf4j.LoggerFactory

import javax.annotation.security.RolesAllowed
import javax.inject.Inject

@Controller("/api/admin")
@Secured(SecurityRule.IS_AUTHENTICATED)
class AdminController {

    @Inject
    PrenotazioneService prenotazioneService
    @Inject
    ConfigurazioneRepository configurazioneRepository

    @Post("/creaSettimana")
    @RolesAllowed(["ADMIN"])
    @Logged
    def creaSettimana(){
        try {
            prenotazioneService.nuovaSettimana()
            [
                    success: true,
                    message: 'ok'
            ]
        }
        catch(all){
            LoggerFactory.getLogger(getClass()).error(Utils.exceptionToString(all))
            [
                    success: false,
                    message: all.message
            ]
        }
    }

    @Get("/parametri")
    @Logged
    List<Configurazione> getParametri(){
        configurazioneRepository.findAllByValidoTrue()
    }

    @Post("/parametri")
    @RolesAllowed(["ADMIN"])
    @Logged
    def salvaConfigurazione(@Body Configurazione configurazione){
        configurazioneRepository.save(configurazione)
    }
}
