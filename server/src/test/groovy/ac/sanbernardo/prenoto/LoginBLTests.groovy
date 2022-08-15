package ac.sanbernardo.prenoto

import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.repositories.UserRepository
import ac.sanbernardo.prenoto.services.UserService
import ac.sanbernardo.prenoto.validators.ValidationException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.*

import java.text.DateFormat
import java.text.SimpleDateFormat

@MicronautTest(application = Application.class,packages = "ac.sanbernardo.prenoto",rollback = true)
class LoginBLTests extends Specification{
    @Inject
    UserRepository userRepository

    @Inject
    UserService userService

    @Shared
    DateFormat df = new SimpleDateFormat('yyyy-MM-dd')
    @Shared
    Date today
    @Shared
    Date yesterday
    @Shared
    Date tomorrow

    void setupSpec(){
        today = new Date()
        Calendar c = GregorianCalendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH,-1)
        yesterday = c.getTime()
        c.add(Calendar.DAY_OF_MONTH,2)
        tomorrow = c.getTime()
    }

    @Unroll
    void "login business logic tests"(){
        given:
        User user1 = new User(nome: 'test',cognome: 'test',password: 'fake',username: 'test')
        user1 = userRepository.save(user1)
        when:
        user1.active = pActive
        user1.dataFineValiditaGreenPass = pDataFineValiditaGreenPass
        user1.dataFineVisitaAgonistica = pDataFineVisitaAgonistica
        user1.role = pRole
        user1 = userRepository.save(user1)
        then:
        User found = userRepository.findByUsernameAndActiveTrue(user1.username).orElse(null)
        try{
            userService.getValidators().each {
                it.validate(found)
            }
        }
        catch(ValidationException ex){
            found = null
        }
        assert (found != null) ==  pFound
        where:
        pActive  |  pDataFineValiditaGreenPass  | pDataFineVisitaAgonistica |  pFound | pRole
        true     | df.parse('1990-01-01')       | yesterday                 | false   | User.Roles.USER.name()
        true     | df.parse('1990-01-01')       | today                     | false   | User.Roles.USER.name()
        true     | df.parse('1990-01-01')       | tomorrow                  | false   | User.Roles.USER.name()
        true     | today                        | yesterday                 | false   | User.Roles.USER.name()
        true     | today                        | today                     | false   | User.Roles.USER.name()
        true     | today                        | tomorrow                  | false   | User.Roles.USER.name()
        true     | yesterday                    | yesterday                 | false   | User.Roles.USER.name()
        true     | yesterday                    | today                     | false   | User.Roles.USER.name()
        true     | yesterday                    | tomorrow                  | false   | User.Roles.USER.name()
        true     | tomorrow                     | yesterday                 | false   | User.Roles.USER.name()
        true     | tomorrow                     | today                     | false   | User.Roles.USER.name()
        true     | tomorrow                     | tomorrow                  | true    | User.Roles.USER.name()
        true     | null                         | null                      | false   | User.Roles.USER.name()
        true     | null                         | yesterday                 | false   | User.Roles.USER.name()
        true     | null                         | today                     | false   | User.Roles.USER.name()
        true     | null                         | tomorrow                  | false   | User.Roles.USER.name()
        true     | yesterday                    | null                      | false   | User.Roles.USER.name()
        true     | today                        | null                      | false   | User.Roles.USER.name()
        true     | tomorrow                     | null                      | false   | User.Roles.USER.name()
        true     | null                         | null                      | true    | User.Roles.OPERATOR.name()
        true     | yesterday                    | null                      | true    | User.Roles.OPERATOR.name()
        true     | today                        | null                      | true    | User.Roles.OPERATOR.name()
        true     | tomorrow                     | null                      | true    | User.Roles.OPERATOR.name()
        true     | null                         | yesterday                 | true    | User.Roles.OPERATOR.name()
        true     | null                         | today                     | true    | User.Roles.OPERATOR.name()
        true     | null                         | tomorrow                  | true    | User.Roles.OPERATOR.name()
        true     | today                        | yesterday                 | true    | User.Roles.OPERATOR.name()
        true     | tomorrow                     | yesterday                 | true    | User.Roles.OPERATOR.name()
        true     | tomorrow                     | tomorrow                  | true    | User.Roles.OPERATOR.name()

    }
}
