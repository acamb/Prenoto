package ac.sanbernardo.prenoto.services

import ac.sanbernardo.prenoto.auth.BcryptPasswordEncoderService
import ac.sanbernardo.prenoto.auth.PasswordEncoder
import ac.sanbernardo.prenoto.controllers.payloads.AggiornaUtenteRequest
import ac.sanbernardo.prenoto.controllers.payloads.ResetPasswordRequest
import ac.sanbernardo.prenoto.model.Configurazione
import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.repositories.ConfigurazioneRepository
import ac.sanbernardo.prenoto.repositories.UserRepository
import ac.sanbernardo.prenoto.validators.Validator
import ac.sanbernardo.prenoto.validators.user.UserGreenPassValidator
import jakarta.inject.Inject
import jakarta.inject.Singleton
import javax.transaction.Transactional

@Singleton
@Transactional
class UserService {

    @Inject
    UserRepository userRepository
    @Inject
    BcryptPasswordEncoderService encoder
    @Inject
    ConfigurazioneRepository configurazioneRepository

    User getUser(String username){
        userRepository.findByUsername(username)
    }

    User login(String username,String password){
        User user =  userRepository.findByUsernameAndActiveTrue(username).orElse(null)
        getValidators().each {
            it.validate(user)
        }
        if(encoder.matches(password,user.password)){
            return user
        }
        else{
            throw new RuntimeException("bad credentials")
        }
    }

    void aggiornaPassword(User user, String oldPassword, String newPassword){
        User userDb = userRepository.findById(user.id).get()
        if(!encoder.matches(oldPassword,userDb.password)){
            throw new RuntimeException("wrong password")
        }
        userDb.password = encoder.encode(newPassword)
        userDb.cambioPassword = false
        userRepository.save(userDb)
    }

    List<User> getAllUsers(){
        return userRepository.findAllOrderByUsername()
    }

    void aggiornaUtente(AggiornaUtenteRequest aggiornaUtenteRequest) {
        User userDb = userRepository.findById(aggiornaUtenteRequest.id).get()
        userDb.nome = aggiornaUtenteRequest.nome
        userDb.cognome = aggiornaUtenteRequest.cognome
        userDb.active = aggiornaUtenteRequest.active
        userDb.dataFineValiditaGreenPass = aggiornaUtenteRequest.dataFineValiditaGreenPass
        userDb.dataFineVisitaAgonistica = aggiornaUtenteRequest.dataFineVisitaAgonistica
        userRepository.save(userDb)
    }

    void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User userDb = userRepository.findById(resetPasswordRequest.id).get()
        userDb.password = encoder.encode(userDb.username.toLowerCase())
        userDb.cambioPassword = true
        userDb.active = true
        userRepository.save(userDb)
    }

    User inserisciUtente(User user) {
        return userRepository.save(user)
    }

    List<Validator<User>> getValidators(){
        List<Validator<User>> validators = []
        if(configurazioneRepository.findByChiaveAndValidoTrue(Configurazione.ConfigTokens.CHECK_GREENPASS.name()).orElse(new Configurazione(valore: "1"))?.valore){
            validators << new UserGreenPassValidator()
        }
        return validators
    }
}
