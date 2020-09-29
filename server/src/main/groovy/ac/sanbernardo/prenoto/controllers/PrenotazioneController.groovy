package ac.sanbernardo.prenoto.controllers

import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.controllers.payloads.IscriviRequestBody
import ac.sanbernardo.prenoto.exceptions.MaxNumeroIscrizioniSuperateException
import ac.sanbernardo.prenoto.exceptions.NumeroOreException
import ac.sanbernardo.prenoto.exceptions.PostiEsauritiException
import ac.sanbernardo.prenoto.model.Prenotazione
import ac.sanbernardo.prenoto.model.SlotPrenotazione
import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.services.PrenotazioneService
import ac.sanbernardo.prenoto.services.UserService
import io.micronaut.context.annotation.Parameter
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

import javax.annotation.security.RolesAllowed
import javax.inject.Inject
import javax.persistence.OptimisticLockException
import java.security.Principal

@Controller("/api/prenotazione")
@Secured(SecurityRule.IS_AUTHENTICATED)
class PrenotazioneController {
    @Inject
    PrenotazioneService prenotazioneService
    @Inject
    UserService userService

    @Get("/slotAttivi")
    @Logged
    @RolesAllowed(["USER","OPERATOR","ADMIN"])
    def getSlotsAttivi(){
        [ giorni:
            prenotazioneService.getSlotsAttuali().groupBy {it.giornoSettimana}.collect { giorno ->
                [
                        giorno: giorno.key,
                        slots : giorno.value.collect{ slot ->
                            [
                                    id: slot.id,
                                    giorno: slot.giornoSettimana,
                                    ora : slot.ora,
                                    posti: slot.postiRimanenti,
                                    visibile: slot.data.after(new Date()),
                                    iscritti: getIscrittiPerSlot(slot.id)
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
        }catch(all){
            [
                    success: false,
                    message: "E_GENERICO"
            ]
        }

    }

    @Get("/")
    @Logged
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
    @Logged
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
