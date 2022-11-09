package ac.sanbernardo.prenoto.controllers.payloads

import ac.sanbernardo.prenoto.model.SlotPrenotazione
import ac.sanbernardo.prenoto.model.TipoIscrizione
import ac.sanbernardo.prenoto.model.User

class IscriviRequestBody {

    User user
    SlotPrenotazione slot
    int ore
    TipoIscrizione tipoIscrizione = TipoIscrizione.UTENTE


    @Override
    public String toString() {
        return "IscriviRequestBody{" +
                "user=" + user +
                ", slot=" + slot +
                ", ore=" + ore +
                ", tipoIscrizione=" + tipoIscrizione +
                '}';
    }
}
