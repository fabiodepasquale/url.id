package test;

import core.*;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestShortURL {

    @Test
    public void testAddSUEntry() throws IOException {
        String shortUrl = "http://tinyurl.com/na4ngfn";
        String longUrl = "https://www.youtube.com/?gl=IT&hl=it";
        String pageTitle = "Youtube";
        String password = "";
        long expireTime = (long)0;
        int expireClicks = 0;
        String status = "OK";
        long createdTime = 1277288524000L;

        boolean res = ShortURL.addSUEntry(shortUrl, longUrl, pageTitle, password, expireTime, expireClicks, status, createdTime);
        assertTrue(res);
    }

    @Test
    public void testSetShortUrl() throws IOException, ParseException {
        String shortUrl = "http://tinyurl.com/na4ngfn";
        String longUrl = "https://www.youtube.com/?gl=IT&hl=it";
        String password = "";
        String expireTimeISO = "2016-06-23T12:22:04.004+0200";
        int expireClicks = 0;

        ShortURL su = new ShortURL();
        int res = su.setShortUrl(shortUrl, longUrl, password, expireTimeISO, expireClicks);
        assertEquals(-3, res);
    }

    @Test
    public void testAddClick() throws IOException {
        String shortUrl = "http://tinyurl.com/na4ngfn";

        ShortURL su = new ShortURL();
        boolean res = su.addClick(shortUrl);
        assertTrue(res);
    }

    @Test
    public void testGetShortUrlInfo() throws IOException {
        String shortUrl = "http://tinyurl.com/na4ngfn";

        ShortURL su = new ShortURL();
        boolean res = su.getShortUrlInfo(shortUrl);
        assertTrue(res);
    }

    @Test
    public void testExistShortUrl() throws IOException {
        String shortUrl = "http://tinyurl.com/na4ngfn";

        ShortURL su = new ShortURL();
        int res = su.existShortUrl(shortUrl);
        assertEquals(1, res);
    }

    @Test
    public void testCanBeReused() throws IOException {
        String shortUrl = "http://tinyurl.com/na4ngfn";

        ShortURL su = new ShortURL();
        int res = su.canBeReused(shortUrl);
        assertEquals(1, res);
    }
}
