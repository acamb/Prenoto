package ac.sanbernardo.prenoto.validators

interface Validator<T> {
    void validate(T object) throws ValidationException
}