package ac.sanbernardo.prenoto.auth

import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton
import javax.validation.constraints.NotBlank
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Singleton
class BcryptPasswordEncoderService implements PasswordEncoder {

    org.springframework.security.crypto.password.PasswordEncoder delegate = new BCryptPasswordEncoder()

    String encode(@NotBlank @NonNull String rawPassword) {
        delegate.encode(rawPassword)
    }

    @Override
    boolean matches(@NotBlank @NonNull String rawPassword, @NotBlank @NonNull String encodedPassword) {
        delegate.matches(rawPassword, encodedPassword)
    }
}
