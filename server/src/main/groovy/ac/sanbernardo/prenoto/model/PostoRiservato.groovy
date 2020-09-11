package ac.sanbernardo.prenoto.model


import com.sun.istack.NotNull

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class PostoRiservato {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id
    @NotNull
    Long arciereId
    int giorno
    int ora
    int numeroOre
    boolean valido = true
    TipoIscrizione tipoIscrizione
}
