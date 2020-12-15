package ac.sanbernardo.prenoto.services

import ac.sanbernardo.prenoto.auth.BcryptPasswordEncoderService
import ac.sanbernardo.prenoto.auth.PasswordEncoder
import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.repositories.UserRepository

import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
@Transactional
class UserService {

    @Inject
    UserRepository userRepository
    @Inject
    BcryptPasswordEncoderService encoder

    User getUser(String username){
        userRepository.findByUsername(username)
    }

    User login(String username,String password){
        User user =  userRepository.findByUsernameAndActiveTrue(username)
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
        return userRepository.findAllByActiveTrue();
    }
}
