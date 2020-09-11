package ac.sanbernardo.prenoto.model

import com.sun.istack.NotNull

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class SlotPrenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id
    @NotNull
    int giornoSettimana
    @NotNull
    int ora
    @NotNull
    Date data
    int postiRimanenti
    @NotNull
    boolean active = true
    @NotNull
    boolean initialized = false
}
