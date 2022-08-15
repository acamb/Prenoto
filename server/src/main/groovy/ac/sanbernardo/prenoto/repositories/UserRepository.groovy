package ac.sanbernardo.prenoto.repositories

import ac.sanbernardo.prenoto.model.User
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface UserRepository extends CrudRepository<User,Long>{

    User findByUsernameAndPassword(String username,String password)

    User findByUsername(String username)

    @Query(value="Select * from user u where u.username = :username and active = 1",nativeQuery = true)
    Optional<User> findByUsernameAndActiveTrue(String username)

    List<User> findAllByActiveTrue()

    List<User> findAllOrderByUsername()
}
