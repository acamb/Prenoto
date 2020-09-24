package ac.sanbernardo.prenoto.services

import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.model.PostoRiservato
import ac.sanbernardo.prenoto.model.TipoIscrizione
import ac.sanbernardo.prenoto.repositories.PostoRiservatoRepository

import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
@Transactional
class PostoRiservatoService {

    @Inject
    PostoRiservatoRepository postoRiservatoRepository

    /**
     *
     * @param tipo
     * @return La lista dei posti riservati attivi per il tipo passato
     */
    @Logged
    List<PostoRiservato> getPostiPerTipo(TipoIscrizione tipo){
        postoRiservatoRepository.findByTipoIscrizioneAndValidoTrue(tipo)
    }
    @Logged
    void salvaPostoRiservato(PostoRiservato postoRiservato){
        if(postoRiservato.id){
            PostoRiservato postoRiservatoDb = postoRiservatoRepository.findById(postoRiservato.id).get()
            postoRiservatoDb.giorno = postoRiservato.giorno
            postoRiservatoDb.ora = postoRiservato.ora
            postoRiservatoDb.numeroOre = postoRiservato.numeroOre
            postoRiservatoRepository.save(postoRiservatoDb)
        }
        else {
            postoRiservatoRepository.save(postoRiservato)
        }
    }
    @Logged
    void eliminaPostoRiservato(Long id){
        PostoRiservato posto = postoRiservatoRepository.findById(id).get()
        postoRiservatoRepository.delete(posto)
    }
}
