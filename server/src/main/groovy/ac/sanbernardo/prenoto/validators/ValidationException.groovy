package ac.sanbernardo.prenoto.validators

class ValidationException extends RuntimeException {

    ValidationException(String message){
        super(message)
    }

    ValidationException(String message,Exception e){
        super(message,e)
    }
}
