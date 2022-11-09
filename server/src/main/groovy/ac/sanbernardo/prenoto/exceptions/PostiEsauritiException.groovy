package ac.sanbernardo.prenoto.exceptions

class PostiEsauritiException extends Exception{

    Date data
    int ora

    public PostiEsauritiException(){

    }

    public PostiEsauritiException(Date data, int ora){
        this.data = data
        this.ora = ora
    }
}
