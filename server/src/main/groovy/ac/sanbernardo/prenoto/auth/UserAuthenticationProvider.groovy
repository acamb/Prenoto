package ac.sanbernardo.prenoto.auth

import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.repositories.UserRepository
import ac.sanbernardo.prenoto.services.UserService
import io.micronaut.context.env.Environment
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationException
import io.micronaut.security.authentication.AuthenticationFailed
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.UserDetails
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.reactivestreams.Publisher

import javax.annotation.Nullable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAuthenticationProvider implements  AuthenticationProvider{

    @Inject
    UserService userService
    @Inject
    BcryptPasswordEncoderService passwordEncoderService
    @Inject
    Environment environment

    @Override
    public Publisher<AuthenticationResponse> authenticate(@Nullable HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flowable.create(emitter -> {

            try {
                User user = userService.login(authenticationRequest.getIdentity(),authenticationRequest.getSecret())
                emitter.onNext(new UserDetails(user.username, user.role ? [user.role] : []));
                emitter.onComplete();
            }
            catch (all) {
                emitter.onError(new AuthenticationException(new AuthenticationFailed()));
            }

        }, BackpressureStrategy.ERROR);
    }
}
