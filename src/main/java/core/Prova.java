package core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Prova {
//    private static org.apache.log4j.Logger log = Logger.getLogger(Prova.class);

    private int status;

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {

        System.out.println("In Prova");

    //    testExternalLink_verifyLink(getTestLinks());
      //  testInsertShortLinks();

    //    HBase.truncateAll();
        tryStatisticsMap();

    //    tryStatisticsMap();
    //    System.out.println(ExternalLink.getPageTitle("https://www.facebook.com/"));

        /*
        System.out.println(ExternalLink.getPageTitle("http://facebook.com"));
        System.out.println(ExternalLink.getPageTitle("https://www.google.it/search?q=slf4j+disable+debug&ie=utf-8&oe=utf-8&gws_rd=cr&ei=GieIVZWbDILoywP_n5-wAQ"));
        System.out.println(ExternalLink.getPageTitle("http://torrent-tv.ru/index.php"));
        System.out.println(ExternalLink.getPageTitle("https://www.facebook.com/"));
        System.out.println(ExternalLink.getPageTitle("https://www.airpair.com/angularjs/building-angularjs-app-tutorial"));
*/
        /*
        String shortUrl = "";
        String longUrl = "http://google.com";
        String password = null;
        String expireTimeISO = "0";
        int expireClicks = 0;

        System.out.println("Try to add new ShortUrl");
        int status = ShortURL.setShortUrl(shortUrl, longUrl, password, expireTimeISO, expireClicks);
        System.out.printf("\nStatus: %d\n", status);
        */
    }


    public static void testExternalLink_verifyLink(List<String> links) throws IOException {
        boolean status;
        long timeout;
        ExternalLink lk = new ExternalLink();

        for (String link : links) {
            System.out.println("TEST " + link);
            timeout = System.currentTimeMillis();
            status = lk.verifyLink(link);
            timeout = System.currentTimeMillis() - timeout;
            System.out.println("Status: " + status + "; Execution time: " + timeout + "\n");
        }
    }

    public static List<String> getTestLinks() throws UnsupportedEncodingException {
        List<String> links = new ArrayList<>();
        links.add("http://google.com");
        links.add("http://download.piriform.com/ccsetup506.exe");
        links.add("http://ladio.ru/flash/en/index_.html#");
        links.add("http://notexistentdomain.com/");
        links.add("http://www.cuoma.com/404");
        links.add("https://www.google.it/search?q=google+maps&ie=utf-8&oe=utf-8&gws_rd=cr&ei=4eWHVca6G4WjyAO8u4CYBQ");
        return links;
    }

    public static void testInsertShortLinks() throws IOException, ParseException, InterruptedException {
  //      setShortUrl(String shortUrl, String longUrl, String password, String expireTimeISO, int expireClicks)
        ShortURL su = new ShortURL();
        int status;

        System.out.println(1);
        status = su.setShortUrl("", "http://google.com", "", "0", 0); // public
        System.out.println("(\"\", \"http://google.com\", \"\", \"0\", 0)"+ "  | status: " + status + "  | shortURL: " + su.shortUrl);

        System.out.println(2);
        status = su.setShortUrl("GGL", "http://google.com", "", "0", 0); // public w/ su
        System.out.println("(\"GGL\", \"http://google.com\", \"\", \"0\", 0)"+ "  | status: " + status + "  | shortURL: " + su.shortUrl);

        System.out.println(3);
        status = su.setShortUrl("", "http://google.com", "pass123", "0", 0); // private w/pass
        System.out.println("(\"\", \"http://google.com\", \"pass123\", \"0\", 0)"+ "  | status: " + status + "  | shortURL: " + su.shortUrl);

        System.out.println(4);
        status = su.setShortUrl("", "http://google.com", "", "0", 0); // public already existent
        System.out.println("(\"\", \"http://google.com\", \"\", \"0\", 0)"+ "  | status: " + status + "  | shortURL: " + su.shortUrl);
    }

    public static void tryStatisticsMap() {

        Statistics s = new Statistics();
        ShortURL su = new ShortURL();

        Timestamp ts = new Timestamp();

        long time = ts.getTimestamp();

        System.out.println(time);


        try {
        //    su.setShortUrl("shortu1", "http://www.google.it", "", "0", 0);

        //    su.getShortUrlInfo("shortu1");

        //    System.out.println(su.shortUrl);

            s.getStats("shortu1", (long) 0, time);
            System.out.println(s.browser.keySet());





            Iterator<String> keys = s.browser.keySet().iterator();

            while (keys.hasNext()) {
                String key = keys.next();
                Integer value = s.browser.get(key);
                System.out.println(key + " value: " + value);
            }




        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}