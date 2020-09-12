package ac.sanbernardo.prenoto.model

import javax.persistence.Version
import javax.validation.constraints.NotNull

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

    @Override
    public String toString() {
        return "Configurazione{" +
                "id=" + id +
                ", chiave='" + chiave + '\'' +
                ", valore='" + valore + '\'' +
                ", valido=" + valido +
                '}';
    }

    enum ConfigTokens {
        POSTI_PER_ORA,
        ORA_INIZIO,
        ORA_FINE,
        NUMERO_ORE_MAX,
        TOKEN_FE,
        ORE_VALIDITA_TOKEN
    }


}
