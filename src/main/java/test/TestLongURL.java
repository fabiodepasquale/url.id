package test;

import core.*;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestLongURL {

    @Test
    public void testAddLUEntry() throws IOException {
        String longUrl = "https://www.youtube.com/?gl=IT&hl=it";
        String shortUrl = "http://tinyurl.com/na4ngfn";
        long timestamp = 1434988387488L;

        boolean res = LongURL.addLUEntry(longUrl, shortUrl, timestamp);
        assertTrue(res);
    }

    @Test
    public void testGetShortUrlsList() throws IOException {
        String longURL = "https://www.youtube.com/?gl=IT&hl=it";

        LongURL lu = new LongURL();
        boolean res = lu.getShortUrlsList(longURL);
        assertTrue(res);
    }

    @Test
    public void testSearchPublicShortUrl() throws IOException {
        String longURL = "https://www.youtube.com/?gl=IT&hl=it";

        LongURL lu = new LongURL();
        int res = lu.searchPublicShortUrl(longURL);
        assertEquals(1, res);
    }

}

