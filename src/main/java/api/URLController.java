package api;

import core.ShortURL;
import core.Statistics;
import core.SystemConfig;
import core.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.apache.logging.log4j.core.util.NameUtil.md5;

@RestController
@RequestMapping("/api/v1/url")
public class URLController {
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody JSON addNewLink(
            @RequestParam(value = "longUrl") String longUrl,
            @RequestParam(value = "shortUrl", defaultValue = "") String shortUrl,
            @RequestParam(value = "pass", defaultValue = "") String password,
            @RequestParam(value = "extime", defaultValue = "0") String expireTime,
            @RequestParam(value = "exclicks", defaultValue = "0") String expireClicksS
    ) throws Exception{

        long expireClicks;

        //long url control
        if (longUrl == null || longUrl.isEmpty()) {
            throw new IllegalArgumentException("longUrl");
        }

        if(!longUrl.substring(longUrl.length() - 1).equals("/")) {
            longUrl = longUrl + "/";
        }

        //shortUrl control
        if(!shortUrl.equals("")) {
            if(!shortUrl.matches("^[-a-zA-Z0-9+]*$"))
            throw new IllegalArgumentException("shortUrl");
        }

        // extime control
        if (!expireTime.equals("0")) {
            if(!expireTime.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}"))
                throw new IllegalArgumentException("extime");
            else {
                expireTime = expireTime + ".000+0000";
            }
        }

        // exclicks control
        if (!expireClicksS.equals("0")) {
            try {
                Integer.parseInt(expireClicksS);
                expireClicks = Long.parseLong(expireClicksS);
            }catch(NumberFormatException e) {
                throw new IllegalArgumentException("exclicks");
            }
        }
        else {
            expireClicks = Long.parseLong(expireClicksS);
        }

        ShortURL su = new ShortURL();

        int result = su.setShortUrl(shortUrl, longUrl, password, expireTime, expireClicks);

        if (result == -1) {  // HBase is down
            return new ErrorJSON(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
        else if (result == -2) {    // link not found
            return new ErrorJSON(new ErrorEntity("invalid", "Resource isn't accessible", "parameter", "longUrl"),
                                 HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
        }
        else if (result == -3) {    // shortUrl already exists
            return new ErrorJSON(new ErrorEntity("invalid", "ShortURL already exists. Please, choose another one", "parameter", "shortUrl"),
                    HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase());
        }
        else {     // added to DB
            return new LinkJSON(su.shortUrl, longUrl, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        }
    }


    @RequestMapping(method = RequestMethod.GET)
    public JSON getLongUrl (@RequestParam(value = "shortUrl", defaultValue = "") String shortUrl,
                            @RequestParam(value = "pass", defaultValue = "") String pass) throws IOException, ServletException {

        if (shortUrl.equals("")) {
            return new ErrorJSON(new ErrorEntity("required", "Required", "parameter", "shortUrl"), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }

        ShortURL su = new ShortURL();
        int status = su.existShortUrl(shortUrl);

        if (status == -1) {     // HBase is down
            return new ErrorJSON(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
        else if (status == 0) {   // shortUrl wasn't found
            return new ErrorJSON(new ErrorEntity("invalid", SystemConfig.domain + shortUrl + " doesn't exist", "parameter", "shortUrl"),
                    HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
        }

        if (!su.password.equals("d41d8cd98f00b204e9800998ecf8427e")) {      // shortUrl isn't public
            if (pass.equals("")) {
                return new ErrorJSON(new ErrorEntity("restricted", "Access denied for " + SystemConfig.domain + shortUrl, "parameter", "password"),
                        HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            }
            else {
                if (su.password.equals(md5(pass))) {
                    UserInfo ui = new UserInfo();
                    ui.getUserAgent(request);
                    ui.getCountryCode(ui.ip);
                    Statistics.addSEntry(shortUrl, ui.ip, ui.countryCode, ui.browser, ui.OS);
                    su.addClick(shortUrl);
                    return new LinkJSON(shortUrl, su.longUrl, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                }
                else {
                    return new ErrorJSON(new ErrorEntity("restricted", "Access denied for " + SystemConfig.domain + shortUrl, "parameter", "password"),
                            HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
                }
            }
        }
        else if (su.expireClicks != (long) 0 || su.expireTime != (long) 0) {
            if (su.clicks == su.expireClicks && su.clicks != (long) 0) {     // expired by clicks
                return new ErrorJSON(new ErrorEntity("expired", SystemConfig.domain + shortUrl + " is expired"), HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.getReasonPhrase());
            }
            else if (su.expireTime < Timestamp.getTimestamp() && su.expireTime != (long) 0) {    // expired by time
                return new ErrorJSON(new ErrorEntity("expired", SystemConfig.domain + shortUrl + " is expired"), HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.getReasonPhrase());
            }
            else {
                UserInfo ui = new UserInfo();
                ui.getUserAgent(request);
                ui.getCountryCode(ui.ip);
                Statistics.addSEntry(shortUrl, ui.ip, ui.countryCode, ui.browser, ui.OS);
                su.addClick(shortUrl);
                return new LinkJSON(shortUrl, su.longUrl, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
            }
        }
        else {
            UserInfo ui = new UserInfo();
            ui.getUserAgent(request);
            ui.getCountryCode(ui.ip);
            Statistics.addSEntry(shortUrl, ui.ip, ui.countryCode, ui.browser, ui.OS);
            su.addClick(shortUrl);
            return new LinkJSON(shortUrl, su.longUrl, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        }
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public @ResponseBody JSON missingParam(MissingServletRequestParameterException e) {
        return new ErrorJSON(new ErrorEntity("required", "Required", "parameter", e.getParameterName()), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public @ResponseBody JSON invalidParam(IllegalArgumentException e) {
        return new ErrorJSON(new ErrorEntity("invalid", "Required", "parameter", e.getMessage()), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }
}
