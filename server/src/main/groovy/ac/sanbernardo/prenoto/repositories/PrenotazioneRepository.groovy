package ac.sanbernardo.prenoto.repositories

import ac.sanbernardo.prenoto.model.Prenotazione
import ac.sanbernardo.prenoto.model.User
import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository


@Repository
interface PrenotazioneRepository extends CrudRepository<Prenotazione,Long>{

    @Query(value = """SELECT *
              FROM Prenotazione p
              INNER JOIN Slot_Prenotazione s
              WHERE p.slot_prenotazione_id = s.id
              AND p.user_id = :userId
              AND s.active = 1
              ORDER BY giorno_settimana,ora
            """,nativeQuery = true)
    @Nullable List<Prenotazione> prenotazioniAttive(Long userId)

}
