package ac.sanbernardo.prenoto

class Utils {

    static String exceptionToString(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.toString()
    }

}
