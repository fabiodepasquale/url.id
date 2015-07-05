package api;

import core.ShortURL;
import core.Statistics;
import core.SystemConfig;
import core.Timestamp;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.apache.logging.log4j.core.util.NameUtil.md5;

@RestController
@RequestMapping("/api/v1/stat/{shortUrl}")
public class StatisticsController {

    public List<LinkStatistics> stat = null;
    public List<StatisticsEntity> browsers = null;
    public List<StatisticsEntity> countries = null;
    public List<StatisticsEntity> platforms = null;

    @RequestMapping(method = RequestMethod.GET)
    public JSON getShortUrlStatistics(@PathVariable String shortUrl,
                                      @RequestParam(value="pass", defaultValue="") String pass,
                                      @RequestParam(value = "from",defaultValue = "") String fromTimeISO,
                                      @RequestParam(value = "to",defaultValue = "") String toTimeISO
                                      ) throws Exception{

        ShortURL su = new ShortURL();
        int status = su.existShortUrl(shortUrl);

        if (status == -1) {     // HBase is down
            return new ErrorJSON(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
        else if (status == 0) {   // shortUrl wasn't found
            return new ErrorJSON(new ErrorEntity("invalid", SystemConfig.domain + shortUrl + " doesn't exist", "parameter", "shortUrl"),
                    HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
        }


        long fromTime;
        long toTime;
        int result;

        if (fromTimeISO.equals("")) {
            fromTime = su.createdTime;
        }
        else {
            if(!fromTimeISO.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}"))
                throw new IllegalArgumentException("from");
            else {
                fromTimeISO = fromTimeISO + ".000+0000";
            }

            fromTime = Timestamp.convertToLong(fromTimeISO);
        }

        if (toTimeISO.equals("")) {
            toTime = Timestamp.getTimestamp();
        }
        else {
            if(!toTimeISO.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}"))
                throw new IllegalArgumentException("to");
            else {
                toTimeISO = toTimeISO + ".000+0000";
            }

            toTime = Timestamp.convertToLong(toTimeISO);
        }

        if (!su.password.equals("d41d8cd98f00b204e9800998ecf8427e")) {      // shortUrl isn't public
            if (pass.equals("")) {
                return new ErrorJSON(new ErrorEntity("restricted", "Access denied for " + SystemConfig.domain + shortUrl, "parameter", "password"),
                        HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            }
            else {
                if (su.password.equals(md5(pass))) {
                    Statistics s = new Statistics();
                    result = s.getStats(shortUrl, fromTime, toTime);

                    if (result == -1) // HBase is down
                        return new ErrorJSON(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

                    else {
                        addStatsToJson(s.countryCode, s.browser, s.os);
                        return new LinkJSON(shortUrl, su.longUrl, su.pageTitle, su.status, su.clicks, this.stat, su.expireTime,
                                su.expireClicks, su.createdTime, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                    }
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
                Statistics s = new Statistics();
                result = s.getStats(shortUrl, fromTime, toTime);

                if (result == -1) // HBase is down
                    return new ErrorJSON(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

                else {
                    addStatsToJson(s.countryCode, s.browser, s.os);
                    return new LinkJSON(shortUrl, su.longUrl, su.pageTitle, su.status, su.clicks, this.stat, su.expireTime,
                            su.expireClicks, su.createdTime, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
                }
            }
        }
        else {
            Statistics s = new Statistics();
            result = s.getStats(shortUrl, fromTime, toTime);

            if (result == -1) // HBase is down
                return new ErrorJSON(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

            else {
                addStatsToJson(s.countryCode, s.browser, s.os);
                return new LinkJSON(shortUrl, su.longUrl, su.pageTitle, su.status, su.clicks, this.stat, su.expireTime,
                        su.expireClicks, su.createdTime, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
            }
        }
    }


    /************* FOR TEST PROPOSE ONLY **************/

    /*
    @RequestMapping(method = RequestMethod.POST)
    public void addStatistics(@RequestParam(value="ip", defaultValue = "unknown") String ip,
                              @RequestParam(value="countryCode", defaultValue = "unknown") String countryCode,
                              @RequestParam(value="browser", defaultValue = "unknown") String browser,
                              @RequestParam(value="os", defaultValue = "unknown") String os,
                              @PathVariable String shortUrl) throws IOException {

        Statistics.addSEntry(shortUrl, ip, countryCode, browser, os);
    }
    */


    private void addStatsToJson(Map<String, Integer> countries, Map<String, Integer> browsers, Map<String, Integer> platforms) {

        Iterator<String> keys = countries.keySet().iterator();
        this.countries = new ArrayList<>();

        while (keys.hasNext()) {
            String key = keys.next();
            Integer value = countries.get(key);
            this.countries.add(new StatisticsEntity(value, key));
        }

        keys = browsers.keySet().iterator();
        this.browsers = new ArrayList<>();

        while (keys.hasNext()) {
            String key = keys.next();
            Integer value = browsers.get(key);
            this.browsers.add(new StatisticsEntity(value, key));
        }

        keys = platforms.keySet().iterator();
        this.platforms = new ArrayList<>();

        while (keys.hasNext()) {
            String key = keys.next();
            Integer value = platforms.get(key);
            this.platforms.add(new StatisticsEntity(value, key));
        }

        this.stat = new ArrayList<>();
        this.stat.add(new LinkStatistics(this.countries, this.browsers, this.platforms));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public @ResponseBody JSON invalidParam(IllegalArgumentException e) {
        return new ErrorJSON(new ErrorEntity("invalid", "Required", "parameter", e.getMessage()), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

}
