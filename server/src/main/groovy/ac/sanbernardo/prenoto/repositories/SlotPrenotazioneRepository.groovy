package ac.sanbernardo.prenoto.repositories

import ac.sanbernardo.prenoto.model.SlotPrenotazione
import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.model.dto.HourTrend
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface SlotPrenotazioneRepository extends CrudRepository<SlotPrenotazione,Long>{

    List<SlotPrenotazione> findByActiveTrueAndInitializedTrue()

    SlotPrenotazione findByGiornoSettimanaAndOra(int giornoSettimana, int ora)

    List<SlotPrenotazione> findByGiornoSettimana(int giornoSettimana)

    @Query(value = "UPDATE SLOT_PRENOTAZIONE set active = 0 where active = 1",nativeQuery = true)
    SlotPrenotazione chiudiSlotAttivi()

    @Query(value = "UPDATE SLOT_PRENOTAZIONE set initialized = 1 where active = 1",nativeQuery = true)
    SlotPrenotazione inizializzaSlotAttivi()

    @Query(value="Select * from user u,prenotazione p where slot_prenotazione_id = :slotId and u.id = p.user_id",nativeQuery = true)
    List<User> getUtentiIscritti(Long slotId)

    @Query(value="""select ORA hour,avg(POSTI_RIMANENTI) value 
            from SLOT_PRENOTAZIONE sp  
            where sp.data  > TODAY -14 
            group by GIORNO_SETTIMANA ,ORA 
            having GIORNO_SETTIMANA  = :day
            order by ora
            """,nativeQuery = true)
    List<HourTrend> getHourlyTrends(int day)

}
