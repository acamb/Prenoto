package ac.sanbernardo.prenoto.repositories

import ac.sanbernardo.prenoto.model.Prenotazione
import ac.sanbernardo.prenoto.model.User
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface PrenotazioneRepository extends CrudRepository<Prenotazione,Long>{

    @Query(value = """SELECT *
              FROM Prenotazione p
              INNER JOIN SlotPrenotazione s
              WHERE p.slot_prenotazione_id = s.id
              AND s.user_id = :userId
              AND s.active = 1
            """,nativeQuery = true)
    List<Prenotazione> prenotazioniAttive(Long userId)

}
