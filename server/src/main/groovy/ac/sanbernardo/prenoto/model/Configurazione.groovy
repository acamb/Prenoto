package ac.sanbernardo.prenoto.model

import com.sun.istack.NotNull

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Configurazione {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id
    @NotNull
    String chiave
    @NotNull
    String valore
    boolean valido = true
}
