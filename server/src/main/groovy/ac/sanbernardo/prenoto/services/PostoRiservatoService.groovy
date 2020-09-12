package ac.sanbernardo.prenoto.services

import ac.sanbernardo.prenoto.aop.Logged
import ac.sanbernardo.prenoto.model.PostoRiservato
import ac.sanbernardo.prenoto.model.TipoIscrizione
import ac.sanbernardo.prenoto.repositories.PostoRiservatoRepository

import javax.inject.Singleton

@Singleton
class PostoRiservatoService {

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
        postoRiservatoRepository.save(postoRiservato)
    }
    @Logged
    void eliminaPostoRiservato(PostoRiservato postoRiservato){
        postoRiservatoRepository.delete(postoRiservato)
    }
}
