package ac.sanbernardo.prenoto.model


import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Version
import javax.validation.constraints.NotNull

@Entity
class PostoRiservato {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id
    @NotNull
    Long userId
    int giorno
    int ora
    int numeroOre
    boolean valido = true
    TipoIscrizione tipoIscrizione


    @Override
    public String toString() {
        return "PostoRiservato{" +
                "id=" + id +
                ", userId=" + userId +
                ", giorno=" + giorno +
                ", ora=" + ora +
                ", numeroOre=" + numeroOre +
                ", valido=" + valido +
                ", tipoIscrizione=" + tipoIscrizione +
                '}';
    }
}
