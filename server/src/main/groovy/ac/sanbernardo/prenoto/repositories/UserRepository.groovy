package ac.sanbernardo.prenoto.repositories

import ac.sanbernardo.prenoto.model.User
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface UserRepository extends CrudRepository<User,Long>{

    User findByUsernameAndPassword(String username,String password)
}
