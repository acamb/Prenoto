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
import io.micronaut.http.annotation.RequestAttribute
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

import javax.annotation.security.RolesAllowed
import javax.inject.Inject

@Controller("/api/postoRiservato")
@Secured(SecurityRule.IS_AUTHENTICATED)
class PostoRiservatoController {

    @Inject
    PostoRiservatoService postoRiservatoService
    @Inject
    UserRepository userRepository

    @Get("/{tipo}")
    @Logged
    @RolesAllowed(["OPERATOR","ADMIN"])
    def getPostiRiservati(TipoIscrizione tipo){
        postoRiservatoService.getPostiPerTipo(tipo).collect{

            User user = userRepository.findById(it.userId).get()
            [
                    giorno:    it.giorno,
                    ora:       it.ora,
                    numeroOre: it.numeroOre,
                    user:      [
                            id: user.id,
                            nome: user.nome,
                            cognome: user.cognome
                    ],
                    tipoIscrizione: it.tipoIscrizione
            ]
        }
    }

    @Post("/")
    @RolesAllowed(["OPERATOR","ADMIN"])
    def save(@Body PostoRiservato postoRiservato){
        postoRiservatoService.salvaPostoRiservato(postoRiservato)
        HttpResponse.ok()
    }

    @Delete("/")
    @RolesAllowed(["OPERATOR","ADMIN"])
    def delete(@RequestAttribute Long id){
        postoRiservatoService.eliminaPostoRiservato(id)
        HttpResponse.ok()
    }
}
