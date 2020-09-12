package ac.sanbernardo.prenoto.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Version

@Entity
class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id
    @ManyToOne
    SlotPrenotazione slotPrenotazione
    Long userId


    @Override
    public String toString() {
        return "Prenotazione{" +
                "id=" + id +
                ", slotPrenotazione=" + slotPrenotazione +
                ", userId=" + userId +
                '}';
    }
}
