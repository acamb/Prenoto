package ac.sanbernardo.prenoto.repositories

import ac.sanbernardo.prenoto.model.SlotPrenotazione
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface SlotPrenotazioneRepository extends CrudRepository<SlotPrenotazione,Long>{

    List<SlotPrenotazione> findByActiveTrueAndInitializedTrue()

    SlotPrenotazione findByGiornoSettimanaAndOra(int giornoSettimana, int ora)

    @Query(value = "UPDATE SLOT_PRENOTAZIONE set active = 0 where active = 1",nativeQuery = true)
    SlotPrenotazione chiudiSlotAttivi()

    @Query(value = "UPDATE SLOT_PRENOTAZIONE set initialized = 1 where active = 1",nativeQuery = true)
    SlotPrenotazione inizializzaSlotAttivi()

}
