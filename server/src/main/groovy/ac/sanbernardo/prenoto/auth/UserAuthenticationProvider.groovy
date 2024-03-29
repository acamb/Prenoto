package ac.sanbernardo.prenoto.auth

import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.repositories.UserRepository
import ac.sanbernardo.prenoto.services.UserService
import ac.sanbernardo.prenoto.validators.ValidationException
import io.micronaut.context.env.Environment
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Inject
import org.reactivestreams.Publisher
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink

@Singleton
class UserAuthenticationProvider implements  AuthenticationProvider{

    @Inject
    UserService userService
    @Inject
    BcryptPasswordEncoderService passwordEncoderService
    @Inject
    Environment environment

    final static Logger log = LoggerFactory.getLogger(UserAuthenticationProvider.class)

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flux.create(emitter -> {

            try {
                User user = userService.login(authenticationRequest.getIdentity(),authenticationRequest.getSecret())
                emitter.next(AuthenticationResponse.success((String) authenticationRequest.identity,[user.role ?: '']))
                emitter.complete();
            }
            catch (all) {
                if(all instanceof ValidationException){
                    log.info("ValidationException [${all.getMessage()}] for [${authenticationRequest.getIdentity()}]")
                }
                emitter.error(AuthenticationResponse.exception());
            }

        }, FluxSink.OverflowStrategy.ERROR);
    }
}
