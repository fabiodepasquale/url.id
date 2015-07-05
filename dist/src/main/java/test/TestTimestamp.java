package test;

import core.Timestamp;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;


public class TestTimestamp {

    @Test
    public void testConvertToISO() {
        long longDate = 1434988387388L;
        String isoDate = "2015-06-22T17:53:07.007+0200";
        String res = Timestamp.convertToISO(longDate);
        assertEquals(isoDate, res);
    }

    @Test
    public void testConvertToLong() throws ParseException {
        long longDate = 1434988387000L;
        String isoDate = "2015-06-22T17:53:07.007+0200";
        long res = Timestamp.convertToLong(isoDate);
        assertEquals(longDate, res);
    }
}
