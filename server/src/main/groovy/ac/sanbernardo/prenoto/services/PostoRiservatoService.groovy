package ac.sanbernardo.prenoto.services

import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.model.PostoRiservato
import ac.sanbernardo.prenoto.model.TipoIscrizione
import ac.sanbernardo.prenoto.repositories.PostoRiservatoRepository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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
    List<PostoRiservato> salvaPostoRiservato(PostoRiservato postoRiservato){
        if(postoRiservato.id){
            PostoRiservato postoRiservatoDb = postoRiservatoRepository.findById(postoRiservato.id)
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
    void eliminaPostoRiservato(PostoRiservato postoRiservato){
        postoRiservatoRepository.delete(postoRiservato)
    }
}
