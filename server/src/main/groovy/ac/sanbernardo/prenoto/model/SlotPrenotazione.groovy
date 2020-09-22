package ac.sanbernardo.prenoto.model

import io.micronaut.data.annotation.Where

import javax.persistence.Table
import javax.persistence.Version
import javax.validation.constraints.NotNull

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity(name = "SLOT_PRENOTAZIONE")
@Where("active = 1")
class SlotPrenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id
    @Version
    int version
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


    @Override
    public String toString() {
        return "SlotPrenotazione{" +
                "id=" + id +
                ", version=" + version +
                ", giornoSettimana=" + giornoSettimana +
                ", ora=" + ora +
                ", data=" + data +
                ", postiRimanenti=" + postiRimanenti +
                ", active=" + active +
                ", initialized=" + initialized +
                '}';
    }
}
