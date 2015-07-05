package core;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class ExternalLink {

    public static Boolean verifyLink(String url) throws IOException {
        Connection.Response response;

        try {
            response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .referrer("google.com")
                    .header("Accept-Language", "en-US,en;q=0.8")
                    .execute();

            int status = response.statusCode();
            return !(status == HttpURLConnection.HTTP_NOT_FOUND || status == HttpURLConnection.HTTP_GONE);

        } catch (IOException e) {
            return checkResponse(url);
        }
    }


    public static String getPageTitle(String url) throws IOException {
        String pageTitle;

        Document doc;

        try {
            doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
                    .referrer("google.com")
                    .header("Accept-Language", "en-US,en;q=0.8")
                    .get();

            return doc.title();
        } catch (IOException e) {
            URL u = new URL(url);
            pageTitle = u.getHost();
        }

        return pageTitle;

    }

    public static Boolean checkResponse(String url) throws IOException {
        try {
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setReadTimeout(5000);
            conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            conn.addRequestProperty("User-Agent", "Mozilla/5.0");
            conn.addRequestProperty("Referrer", "google.com");

            boolean redirect = false;

            // normally, 3xx is redirect
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
                else if (status == HttpURLConnection.HTTP_NOT_FOUND
                        || status == HttpURLConnection.HTTP_GONE)
                    return false;
            }

            if (redirect) {
                // get redirect url from "location" header field
                String newUrl = conn.getHeaderField("Location");

                checkResponse(newUrl);
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }


}
