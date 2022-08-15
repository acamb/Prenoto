package ac.sanbernardo.prenoto.controllers

import ac.sanbernardo.prenoto.Utils
import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.controllers.payloads.IscriviRequestBody
import ac.sanbernardo.prenoto.exceptions.IscrizioneNelPassatoNonCancellabileException
import ac.sanbernardo.prenoto.exceptions.MaxNumeroIscrizioniSuperateException
import ac.sanbernardo.prenoto.exceptions.NumeroOreException
import ac.sanbernardo.prenoto.exceptions.PostiEsauritiException
import ac.sanbernardo.prenoto.services.PrenotazioneService
import ac.sanbernardo.prenoto.services.UserService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import jakarta.inject.Inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.annotation.security.RolesAllowed
import javax.persistence.OptimisticLockException
import java.security.Principal

@Controller("/api/prenotazione")
@Secured(SecurityRule.IS_AUTHENTICATED)
class PrenotazioneController {
    @Inject
    PrenotazioneService prenotazioneService
    @Inject
    UserService userService

    Logger logger = LoggerFactory.getLogger(PrenotazioneController.class)

    @Get("/slotAttivi")
    @RolesAllowed(["USER","OPERATOR","ADMIN"])
    def getSlotsAttivi(){
        [ giorni:
            prenotazioneService.getSlotsAttuali()?.groupBy {it.giornoSettimana}.collect { giorno ->
                [
                        giorno: giorno.key,
                        data: giorno.value?.get(0)?.data,
                        slots : giorno.value.collect{ slot ->
                            [
                                    id: slot.id,
                                    giorno: slot.giornoSettimana,
                                    ora : slot.ora,
                                    posti: slot.postiRimanenti,
                                    visibile: slot.data.after(new Date()),
                                    iscritti: getIscrittiPerSlot(slot.id),
                                    dataOraSlot: slot.getDataOraSlot()
                            ]
                        }.asList()
                ]
            }
        ]
    }

    @Post("/")
    @Logged
    @RolesAllowed(["USER","OPERATOR","ADMIN"])
    def iscrivi(@Body IscriviRequestBody body){
        boolean result = true
        String message = "PRENOTAZIONE_OK"

        try {
            prenotazioneService.prenota(body.user,
                    body.slot,
                    body.ore)
        }
        catch(NumeroOreException ex){
            result = false
            message = "PRENOTAZIONE_KO_ORE"
        }
        catch(PostiEsauritiException ex){
            result = false
            message = "PRENOTAZIONE_KO_POSTO"
        }
        catch(MaxNumeroIscrizioniSuperateException ex){
            result = false
            message = "PRENOTAZIONE_KO_NUM_PRENOTAZIONI"
        }
        catch(OptimisticLockException e){
            result = false
            message = "PRENOTAZIONE_KO_CONCURRENT"
        }
        catch(all){
            logger.error(Utils.exceptionToString(all))
            result = false
            message = "E_GENERICO"
        }

        return [
                success: result,
                message: message
        ]
    }

    @Delete("/")
    @Logged
    @RolesAllowed(["USER","OPERATOR","ADMIN"])
    def cancellaPrenotazione(@QueryValue Long id, Principal principal){
        try {
            prenotazioneService.cancellaPrenotazione(userService.getUser(principal.name), id)
            [
                    success: true
            ]
        }catch(IscrizioneNelPassatoNonCancellabileException ex){
            [
                    success: false,
                    message: 'ISCRIZIONE_NEL_PASSATO_NON_CANCELLABILE'
            ]
        }catch(all){
            logger.error(Utils.exceptionToString(all))
            [
                    success: false,
                    message: "E_GENERICO"
            ]
        }

    }

    @Get("/")
    @RolesAllowed(["USER","OPERATOR","ADMIN"])
    def prenotazioniUtente(Principal principal){


        prenotazioneService
                .getPrenotazioniPerArciere(userService.getUser(principal.name).id)
                .collect{
                    [
                            id: it.id,
                            slotId: it.slotPrenotazione.id,
                            giorno: it.slotPrenotazione.giornoSettimana,
                            ora: it.slotPrenotazione.ora
                    ]
                }
    }

    @Get("/iscrittiPerSlot")
    def getIscrittiPerSlot(@QueryValue Long slotId){
        prenotazioneService.getUtentiIscritti(slotId)
            .collect{
                [
                        nome: it.nome,
                        cognome: it.cognome,
                ]
            }
    }

}
