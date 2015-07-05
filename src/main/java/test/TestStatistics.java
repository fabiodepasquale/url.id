package test;

import core.Statistics;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;


public class TestStatistics {

    @Test
    public void testAddSEntry() throws IOException {

        String shortUrl = "http://tinyurl.com/na4ngfn";
        String ip = "192.168.1.2";
        String countryCode = "IT";
        String browser = "Safari";
        String os = "Windows 10";
        boolean res = Statistics.addSEntry(shortUrl, ip, countryCode, browser, os);
        assertTrue(res);
    }

    @Test
    public void testGetStats() throws IOException {

        String shortUrl = "http://tinyurl.com/na4ngfn";
        long maxtimestamp = (long)0;
        String password = "";

        Statistics s = new Statistics();
    //    boolean res = s.getStats(shortUrl, maxtimestamp, password);
    //    assertTrue(res);
    }
}
