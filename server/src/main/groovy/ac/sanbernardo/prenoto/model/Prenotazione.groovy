package ac.sanbernardo.prenoto.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id
    @ManyToOne
    SlotPrenotazione slotPrenotazione
    Long arciereId
}
