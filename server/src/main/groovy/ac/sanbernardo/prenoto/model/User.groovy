package ac.sanbernardo.prenoto.model

import ac.sanbernardo.prenoto.annotations.Uppercase
import ac.sanbernardo.prenoto.traits.UppercaseTrait
import com.sun.istack.NotNull
import org.hibernate.annotations.Generated

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

@Entity
class User implements UppercaseTrait {

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

    @PrePersist
    void prePersist(){
        setUppercaseFields()
    }

    @PreUpdate
    void preUpdate(){
        setUppercaseFields()
    }
}
