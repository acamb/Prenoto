package ac.sanbernardo.prenoto.controllers

import ac.sanbernardo.prenoto.controllers.payloads.CambioPasswordRequest
import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.services.UserService
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

import javax.annotation.Nullable
import javax.inject.Inject
import java.security.Principal

@Controller("/api/user")
@Secured(SecurityRule.IS_AUTHENTICATED)
class UserController {

    @Inject
    UserService userService

    @Get("/")
    def getUser(@Nullable Principal principal){
        User user = userService.getUser(principal.getName())
        [
                id: user.id,
                username: user.username,
                nome: user.nome,
                cognome: user.cognome,
                active: user.active,
                cambioPassword: user.cambioPassword,
                role: user.role
        ]
    }
    @Post('/cambiaPassword')
    def cambiaPassword(@Body CambioPasswordRequest request,@Nullable Principal){
        User user = userService.getUser(principal.getName())
        try {
            userService.aggiornaPassword(user, request.vecchiaPassword, request.nuovaPassword)
            [
                    success: true
            ]
        }
        catch(all){
            [
                    success: false,
                    message: 'password.mismatch'
            ]
        }
    }
}
