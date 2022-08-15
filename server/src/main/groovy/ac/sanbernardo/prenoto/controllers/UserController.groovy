package ac.sanbernardo.prenoto.controllers

import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.controllers.payloads.AggiornaUtenteRequest
import ac.sanbernardo.prenoto.controllers.payloads.CambioPasswordRequest
import ac.sanbernardo.prenoto.controllers.payloads.NuovoUtenteRequest
import ac.sanbernardo.prenoto.controllers.payloads.ResetPasswordRequest
import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.repositories.UserRepository
import ac.sanbernardo.prenoto.services.UserService
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import jakarta.inject.Inject
import org.slf4j.LoggerFactory

import javax.annotation.security.RolesAllowed
import javax.management.relation.Role
import javax.validation.Valid
import java.security.Principal

@Controller("/api/user")
@Secured(SecurityRule.IS_AUTHENTICATED)
class UserController {

    @Inject
    UserService userService

    @Inject
    UserRepository userRepository

    @Get("/list")
    def getUsers(){
        userService.getAllUsers()
            .collect{ user ->
                [
                        id: user.id,
                        username: user.username,
                        nome: user.nome,
                        cognome: user.cognome,
                        active: user.active,
                        cambioPassword: user.cambioPassword,
                        role: user.role,
                        dataFineVisitaAgonistica: user.dataFineVisitaAgonistica,
                        dataFineValiditaGreenPass: user.dataFineValiditaGreenPass
                ]
            }
    }

    @Get("/")
    @Logged
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
    @Logged
    def cambiaPassword(@Body CambioPasswordRequest request,@Nullable Principal principal){
        User user = userService.getUser(principal.getName())
        try {
            userService.aggiornaPassword(user, request.vecchiaPassword, request.nuovaPassword)
            [
                    success: true
            ]
        }
        catch(ignored){
            [
                    success: false,
                    message: 'password.mismatch'
            ]
        }
    }

    @Post('/aggiorna')
    @Logged
    @RolesAllowed(["OPERATOR","ADMIN"])
    def aggiorna(@Body AggiornaUtenteRequest request, @Nullable Principal principal){
        try {
            userService.aggiornaUtente(request)
            [
                    success: true
            ]
        }
        catch(ignored){
            [
                    success: false,
                    message: 'generic.error'
            ]
        }
    }

    @Post('/resetPassword')
    @Logged
    @RolesAllowed(["OPERATOR","ADMIN"])
    def aggiorna(@Body ResetPasswordRequest request, @Nullable Principal principal){
        try {
            userService.resetPassword(request)
            [
                    success: true
            ]
        }
        catch(ignored){
            [
                    success: false,
                    message: 'generic.error'
            ]
        }
    }

    @Get("/get")
    @Logged
    @RolesAllowed(["OPERATOR","ADMIN"])
    def loadUser(@QueryValue Long id){
        User user = userRepository.findById(id).get()
        return userToMap(user)
    }

    @Post("/")
    @Logged
    @RolesAllowed(['OPERATOR','ADMIN'])
    def saveUser(@Valid @Body NuovoUtenteRequest nuovoUtenteRequest){
        User user = new User()
        user.nome = nuovoUtenteRequest.nome
        user.cognome = nuovoUtenteRequest.cognome
        user.password = nuovoUtenteRequest.password
        user.active = nuovoUtenteRequest.active
        user.cambioPassword = nuovoUtenteRequest.cambioPassword
        user.username = nuovoUtenteRequest.username
        user.role = nuovoUtenteRequest.role
        user.dataFineValiditaGreenPass = nuovoUtenteRequest.dataFineValiditaGreenPass
        user.dataFineVisitaAgonistica = nuovoUtenteRequest.dataFineVisitaAgonistica
        try {
            user = userService.inserisciUtente(user)
            return userToMap(user)
        }
        catch(ignored){
            [
                    success: false,
                    message: 'generic.error'
            ]
        }
    }

    static Map userToMap(User user){
        [
                id: user.id,
                username: user.username,
                nome: user.nome,
                cognome: user.cognome,
                active: user.active,
                cambioPassword: user.cambioPassword,
                role: user.role,
                dataFineVisitaAgonistica: user.dataFineVisitaAgonistica,
                dataFineValiditaGreenPass: user.dataFineValiditaGreenPass
        ]
    }
}
