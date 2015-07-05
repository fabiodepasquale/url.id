package test;

import core.ExternalLink;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestExternalLink {

    @Test
    public void testVerifyLink() throws IOException {
        String link = "https://it-it.facebook.com/";
        boolean res = ExternalLink.verifyLink(link);
        assertTrue(res);
    }

    @Test
    public void testGetPageTitle() throws ParseException, IOException {
        String link = "http://www.gotoquiz.com/web-coding/programming/java-programming/how-to-extract-titles-from-web-pages-in-java/";
        String pageTitle = "How to extract titles from web pages in Java | A Web Coding Blog";
        ExternalLink el = new ExternalLink();
        String res = el.getPageTitle(link);
        assertEquals(pageTitle, res);
    }
}
