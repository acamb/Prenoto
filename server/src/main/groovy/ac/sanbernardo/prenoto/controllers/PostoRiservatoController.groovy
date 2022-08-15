package ac.sanbernardo.prenoto.controllers

import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.model.PostoRiservato
import ac.sanbernardo.prenoto.model.TipoIscrizione
import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.repositories.UserRepository
import ac.sanbernardo.prenoto.services.PostoRiservatoService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.annotation.RequestAttribute
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import jakarta.inject.Inject

import javax.annotation.security.RolesAllowed
import java.security.Principal

@Controller("/api/postoRiservato")
@Secured(SecurityRule.IS_AUTHENTICATED)
class PostoRiservatoController {

    @Inject
    PostoRiservatoService postoRiservatoService
    @Inject
    UserRepository userRepository

    @Get("/{tipo}")
    @RolesAllowed(["OPERATOR","ADMIN"])
    def getPostiRiservati(TipoIscrizione tipo){
        postoRiservatoService.getPostiPerTipo(tipo).collect{

            User user = userRepository.findById(it.userId).get()
            [
                    id: it.id,
                    giorno:    it.giorno,
                    ora:       it.ora,
                    numeroOre: it.numeroOre,
                    userId : it.userId,
                    tipoIscrizione: it.tipoIscrizione
            ]
        }
    }

    @Post("/")
    @RolesAllowed(["OPERATOR","ADMIN"])
    @Logged
    def save(@Body PostoRiservato postoRiservato,Principal principal){
        postoRiservatoService.salvaPostoRiservato(postoRiservato)
        HttpResponse.ok()
    }

    @Delete("/")
    @RolesAllowed(["OPERATOR","ADMIN"])
    @Logged
    def delete(@QueryValue Long id, Principal principal){
        postoRiservatoService.eliminaPostoRiservato(id)
        HttpResponse.ok()
    }
}
