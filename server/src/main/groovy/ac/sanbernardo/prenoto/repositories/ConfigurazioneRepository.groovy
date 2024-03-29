package ac.sanbernardo.prenoto.repositories

import ac.sanbernardo.prenoto.model.Configurazione
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface ConfigurazioneRepository extends CrudRepository<Configurazione, Long> {

    Optional<Configurazione> findByChiaveAndValidoTrue(String chiave);

    List<Configurazione> findAllByValidoTrue();

}
