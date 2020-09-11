package ac.sanbernardo.prenoto.repositories

import ac.sanbernardo.prenoto.model.SlotPrenotazione
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface SlotPrenotazioneRepository extends CrudRepository<SlotPrenotazione,Long>{

    List<SlotPrenotazione> findByActiveTrue()

    @Query(value = "SELECT * FROM slot_prenotazione where id = :id FOR UPDATE",nativeQuery = true)
    SlotPrenotazione getAndLock(Long id)

}
