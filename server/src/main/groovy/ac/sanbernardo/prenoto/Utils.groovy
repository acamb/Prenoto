package ac.sanbernardo.prenoto

import java.text.DateFormat
import java.text.SimpleDateFormat

class Utils {

    static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy"

    static String exceptionToString(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        sw.toString()
    }

    static String date2String(Date date){
        if(!date) return ""
        DateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT)
        return df.format(date)
    }

}
