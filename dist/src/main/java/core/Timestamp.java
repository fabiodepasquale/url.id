package core;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timestamp {
    private static org.apache.log4j.Logger log = Logger.getLogger(Timestamp.class);
    public static Long getTimestamp() {
        return System.currentTimeMillis();
    }

    /* Transform current time in ISO 8601 format */
    public static String convertToISO(long longTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ");
        return df.format(longTime);
    }

    public static long convertToLong(String stringTime) throws ParseException {
        if (stringTime.equals("0")) return (long) 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ");
        Date date =  df.parse(stringTime);
        return date.getTime();
    }

}
