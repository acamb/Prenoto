package ac.sanbernardo.prenoto.model

import ac.sanbernardo.prenoto.annotations.Uppercase
import ac.sanbernardo.prenoto.traits.UppercaseTrait

import javax.persistence.Version
import javax.validation.constraints.NotNull

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

@Entity
class User implements UppercaseTrait {

    /*@Version
    int version*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id
    @NotNull
    @Uppercase
    String nome
    @NotNull
    @Uppercase
    String cognome
    @NotNull
    String password
    boolean active = true
    boolean cambioPassword = true
    @NotNull
    String username

    @PrePersist
    void prePersist(){
        setUppercaseFields()
    }

    @PreUpdate
    void preUpdate(){
        setUppercaseFields()
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", cambioPassword=" + cambioPassword +
                '}';
    }
}
