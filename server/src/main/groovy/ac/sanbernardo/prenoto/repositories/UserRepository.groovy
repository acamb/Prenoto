package ac.sanbernardo.prenoto.repositories

import ac.sanbernardo.prenoto.model.User
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface UserRepository extends CrudRepository<User,Long>{

    User findByUsernameAndPassword(String username,String password)

    User findByUsername(String username)

    @Query(value="Select * from user u where u.username = :username and active = 1 and ( (data_fine_validita_green_pass > CURRENT_TIMESTAMP() and data_fine_visita_agonistica > CURRENT_TIMESTAMP()) OR role in ('ADMIN','OPERATOR') ) ",nativeQuery = true)
    Optional<User> findByUsernameAndActiveTrue(String username)

    List<User> findAllByActiveTrue()
}
