package ac.sanbernardo.prenoto.repositories

import ac.sanbernardo.prenoto.model.PostoRiservato
import ac.sanbernardo.prenoto.model.TipoIscrizione
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

import javax.persistence.LockModeType
import java.util.concurrent.locks.Lock

@Repository
interface PostoRiservatoRepository extends CrudRepository<PostoRiservato,Long>{

        List<PostoRiservato> findByTipoIscrizioneAndActiveTrue(TipoIscrizione tipoIscrizione)

}
