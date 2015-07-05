package core;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ShortCode {
    private static org.apache.log4j.Logger log = Logger.getLogger(ShortCode.class);
    public String shortCode;

    public boolean getShortCode(int length) throws IOException {

        Blacklist bl = new Blacklist(); //list<string> bWords = null
        ShortURL su = new ShortURL();

        if(!bl.getBWords()) {
            return false;           // hbase is down
        }                           // bWords

        //generate a shortCode while this is not in Blacklist
        do {
            this.shortCode = RandomStringUtils.randomAlphanumeric(length);
        }
        while (bl.isCensored(shortCode, bl.bWords) || su.existShortUrl(shortCode) == 1);

        return true;        // hbase is up, shortCode is generated and can be accessed by shc.shortCode
    }

}
