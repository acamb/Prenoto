package ac.sanbernardo.prenoto.controllers.payloads

import ac.sanbernardo.prenoto.model.User
import io.micronaut.core.annotation.Introspected

import javax.validation.constraints.NotNull

@Introspected
class NuovoUtenteRequest {
    @NotNull
    String nome
    @NotNull
    String cognome
    @NotNull
    String password
    boolean active = true
    boolean cambioPassword = true
    @NotNull
    String username
    String role = User.Roles.USER.name()
    Date dataFineValiditaGreenPass
    Date dataFineVisitaAgonistica
}
