package ac.sanbernardo.prenoto.controllers.payloads

import ac.sanbernardo.prenoto.model.SlotPrenotazione
import ac.sanbernardo.prenoto.model.User

class IscriviRequestBody {

    User user
    SlotPrenotazione slot
    int ore


    @Override
    public String toString() {
        return "IscriviRequestBody{" +
                "user=" + user +
                ", slot=" + slot +
                ", ore=" + ore +
                '}';
    }
}
