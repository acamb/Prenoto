package ac.sanbernardo.prenoto.validators.user

import ac.sanbernardo.prenoto.model.User
import ac.sanbernardo.prenoto.validators.ValidationException
import ac.sanbernardo.prenoto.validators.Validator

class UserGreenPassValidator implements Validator<User> {
    @Override
    void validate(User object) throws ValidationException {
        if(object == null){
            return
        }
        if(['ADMIN','OPERATOR'].contains(object.role)){
            return
        }
        if( !object.dataFineValiditaGreenPass || !object.dataFineValiditaGreenPass.after(new Date())){
            throw new ValidationException("Green pass scaduto")
        }
        if( !object.dataFineVisitaAgonistica || !object.dataFineVisitaAgonistica.after(new Date())){
            throw new ValidationException("Visita agonistica scaduta")
        }
    }
}
