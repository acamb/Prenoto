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
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

import javax.inject.Inject

@Controller("/api/prenotazione")
@Secured(SecurityRule.IS_AUTHENTICATED)
class PrenotazioneController {
    @Inject
    PrenotazioneService prenotazioneService

    @Get("/slotAttivi")
    @Logged
    def getSlotsAttivi(){
        prenotazioneService.getSlotsAttuali().collect {
            [
                    giorno: it.giornoSettimana,
                    ora: it.ora,
                    posti: it.postiRimanenti
            ]
        }
    }

    @Post("/iscrivi")
    @Logged
    def iscrivi(@Body IscriviRequestBody body){
        Prenotazione p
        String result = true
        String message = "PRENOTAZIONE_OK"

        try {
            p = prenotazioneService.prenota(body.user,
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

        return [
                result: result,
                message: message,
                prenotazioneId: p?.id
        ]
    }

    @Delete("cancella")
    @Logged
    def cancellaPrenotazione(@Body Prenotazione){

        prenotazioneService.cancellaPrenotazione(prenotazione)
        return HttpResponse.ok()

    }

}
