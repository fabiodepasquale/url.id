package api;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import core.ShortURL;
import core.Statistics;
import core.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Controller
public class PageController {
    @Autowired
    private HttpServletRequest request;


    @RequestMapping(value={"/", "/index.html"})
    public String home(){
        return "redirect:/home/index.html";
    }

    @RequestMapping("{shortUrl}_")
    public String analytics(@PathVariable String shortUrl){
        return "redirect:/home/statistics.html?shortUrl="+shortUrl;
    }

    @RequestMapping("{shortUrl}")
    public String redirect(@PathVariable String shortUrl) throws IOException, ServletException, GeoIp2Exception {
        System.out.println("SU: "+shortUrl);
        ShortURL su = new ShortURL();
        int status = su.existShortUrl(shortUrl);

        if (status == -1) {     // HBase is down
            return "redirect:/home/error.html?reason=server-error";
        }
        else if (status == 0) {   // shortUrl wasn't found
            return "redirect:/home/404.html";
        }

        if (!su.password.equals("d41d8cd98f00b204e9800998ecf8427e")) {
            return "redirect:/home/redirect.html?shortUrl="+shortUrl;
        }
        else if (su.expireClicks != (long) 0 || su.expireTime != (long) 0) {
            if (su.clicks == su.expireClicks && su.clicks != (long) 0) {     // expired by clicks
                return "redirect:/home/error.html?reason=expired";
            }
            else if (su.expireTime < Timestamp.getTimestamp() && su.expireTime != (long) 0) {    // expired by time
                return "redirect:/home/error.html?reason=expired";
            }
            else {
                UserInfo ui = new UserInfo();
                ui.getUserAgent(request);
                ui.getCountryCode(ui.ip);

                Statistics.addSEntry(shortUrl, ui.ip, ui.countryCode, ui.browser, ui.OS);
                su.addClick(shortUrl);
                return "redirect:" + su.longUrl;
            }
        }
        else {
            UserInfo ui = new UserInfo();
            ui.getUserAgent(request);
            ui.getCountryCode(ui.ip);

            Statistics.addSEntry(shortUrl, ui.ip, ui.countryCode, ui.browser, ui.OS);
            su.addClick(shortUrl);
            return "redirect:" + su.longUrl;
        }
    }

}