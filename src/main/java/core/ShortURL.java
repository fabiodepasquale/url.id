package core;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.ParseException;

import static org.apache.logging.log4j.core.util.NameUtil.md5;


public class ShortURL {

    public String shortUrl = null;
    public String longUrl = null;
    public String password = null;
    public long expireTime = (long) 0;
    public long expireClicks = (long) 0;
    public String pageTitle = null;
    public long clicks = (long) 0;
    public String status = null;
    public long createdTime;


    /**
     * @implNote Collect all components of shortUrl, setting all those missing,
     *           and call addSUEntry method for add record to DB
     * @apiNote Method assigns shortUrl class variable that can be used from API
     * @throws IOException, ParseException
     * @return -1 if HBase is down; -2 if url not found, -3 if shortUrl indicated
     *          by user already exists,  0 if all ok
     */
    public int setShortUrl(String shortUrl, String longUrl, String password, String expireTimeISO, long expireClicks) throws IOException, ParseException {

        // control if link exists, otherwise exit with error code -2
        if(!ExternalLink.verifyLink(longUrl)) {
            return -2;
        }

        // set missing variables to add to query
        ExternalLink el = new ExternalLink();
        String pageTitle = el.getPageTitle(longUrl);
        String status = "OK";
        long expireTime = Timestamp.convertToLong(expireTimeISO); // throw exception
        long createdTime = Timestamp.getTimestamp();

        // if shortUrl isn't specified...
        if (shortUrl.equals("")) {
            // ... and other options are not specified -> create public shortUrl
            if (password.equals("") && expireClicks == (long) 0 && expireTime == (long) 0) {

                LongURL lu = new LongURL();
                if(lu.searchPublicShortUrl(longUrl) == 1) {       // ... search for existing one...
                    this.shortUrl = lu.shortUrl;                  // ... and assign it to class variable
                    return 0;
                }
                else {                                            // if not found -> generate random one
                    ShortCode code = new ShortCode();

                    if(!code.getShortCode(5)) {
                        return -1;  // exit with error code -1 (HBase is down)
                    }

                    this.shortUrl = code.shortCode; // import generated shortUrl from ShortCode.class
                    shortUrl = this.shortUrl;
                 }
            }
            // otherwise create a private one
            else {
                ShortCode code = new ShortCode();

                if(!code.getShortCode(5)) {
                    return -1;  // exit with error code -1 (HBase is down)
                }

                this.shortUrl = code.shortCode; // import generated shortUrl from ShortCode.class
                shortUrl = this.shortUrl;
            }
        }
        else if(existShortUrl(shortUrl) == 1) return -3; // already exists

        // try to add record to DB...

        // ...firstly to shortUrl table
        boolean result = addSUEntry(shortUrl, longUrl, pageTitle, password, expireTime, expireClicks, status, createdTime);
        if(!result) return -1;  // HBase is down

        // ...than add alias in longUrl table
        result = LongURL.addLUEntry(longUrl, shortUrl, createdTime);
        if(!result) return -1;  // HBase is down

        this.shortUrl = shortUrl;   // assign class variable for return generated link to API
        return 0;   // exit without errors
    }


    /**
     * @implNote Add new row to shortUrl table
     * @throws IOException
     * @return "false" if HBase is down; "true" if entry was inserted to DB
     */
    public static boolean addSUEntry (String shortUrl, String longUrl, String pageTitle, String password, long expireTime, long expireClicks, String status, long createdTime) throws IOException{
        Connection conn = ConnectionFactory.createConnection();

        if(!SystemConfig.checkConn()) {
            return false;                   //  HBase is down
        }

        Table table = conn.getTable(TableName.valueOf("su"));

        long clicks = (long) 0;

        // md5(shortUrl) as a RowKey
        Put p = new Put(Bytes.toBytes(md5(shortUrl)));

        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("su"), Bytes.toBytes(shortUrl));
        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("lu"), Bytes.toBytes(longUrl));
        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("pt"), Bytes.toBytes(pageTitle));
        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("pw"), Bytes.toBytes(md5(password)));
        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("et"), Bytes.toBytes(expireTime));
        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("ec"), Bytes.toBytes(expireClicks));
        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("cl"), Bytes.toBytes(clicks));
        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("st"), Bytes.toBytes(status));
        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("ct"), Bytes.toBytes(createdTime));

        // Saving the Put instance to the table
        table.put(p);
        table.close();
        conn.close();

        return true;    // exit without errors
    }


    /**
     * @implNote Update 'clicks' column for specified shortUrl (add +1)
     * @throws IOException
     * @return "false" if HBase is down; "true" if entry was updated in DB
     */
    public boolean addClick (String shortUrl) throws IOException {

        Connection conn = ConnectionFactory.createConnection();

        if(!SystemConfig.checkConn()) {
            return false;                     //  HBase is down
        }

        Table table = conn.getTable(TableName.valueOf("su"));

        Increment inc = new Increment(Bytes.toBytes(md5(shortUrl)));
        inc.addColumn(Bytes.toBytes("d"), Bytes.toBytes("cl"), 1);
        table.increment(inc);

        table.close();
        conn.close();
        return true;
    }


    /**
     * @implNote Get all information about shortUrl from DB; Existence of the ShortUrl must be controlled before
     * @throws IOException
     * @return "false" if HBase is down; "true" if entry was updated in DB;
     *          in success case fills up class variables from shortUrl's table record
     */
    public boolean getShortUrlInfo(String shortUrl) throws IOException {

        Connection conn = ConnectionFactory.createConnection();

        if(!SystemConfig.checkConn()) {
            return false;                     //  HBase is down
        }

        Table table = conn.getTable(TableName.valueOf("su"));

        // Instantiating the Scan class
        Get g = new Get(Bytes.toBytes(md5(shortUrl)));
        //add to return all the Column Family
        g.addFamily( Bytes.toBytes("d"));

        Result r = table.get(g);

        if(r.isEmpty()){
            this.shortUrl = null;
        }

        else{
            //extract, convert value and assign it to class variable
            this.shortUrl = Bytes.toString(r.getValue(Bytes.toBytes("d"), Bytes.toBytes("su")));
            longUrl = Bytes.toString(r.getValue(Bytes.toBytes("d"), Bytes.toBytes("lu")));
            password = Bytes.toString(r.getValue(Bytes.toBytes("d"), Bytes.toBytes("pw")));
            expireTime = Bytes.toLong(r.getValue(Bytes.toBytes("d"), Bytes.toBytes("et")));
            expireClicks = Bytes.toLong(r.getValue(Bytes.toBytes("d"), Bytes.toBytes("ec")));
            pageTitle = Bytes.toString(r.getValue(Bytes.toBytes("d"), Bytes.toBytes("pt")));
            clicks = Bytes.toLong(r.getValue(Bytes.toBytes("d"), Bytes.toBytes("cl")));
            status = Bytes.toString(r.getValue(Bytes.toBytes("d"), Bytes.toBytes("st")));
            createdTime = Bytes.toLong(r.getValue(Bytes.toBytes("d"), Bytes.toBytes("ct")));
        }

        table.close();
        conn.close();
        return true;
    }


    /**
     * @implNote Control if shortUrl exists in DB
     * @throws IOException
     * @return -1 if HBase is down; 0 if shortUrl wasn't found; 1 if shortUrl exists in DB
     */
    public int existShortUrl(String shortUrl) throws IOException {

        // Call getShortUrlInfo to extract all information from shortUrl
        if(!getShortUrlInfo(shortUrl)){
            return -1;              // HBase is down
        }

        if(this.shortUrl == null){
            return 0;  // shortUrl not found in DB
        }

        return 1; // exists

    }

    /**
     * @implNote Control if existent shortUrl is public and can be reused
     * @throws IOException
     * @return -1 if HBase is down; 0 if shortUrl is private; 1 if can be reused
     */
    public int canBeReused(String shortUrl) throws IOException {

        int status = existShortUrl(shortUrl);

        if (status == -1) return -1; // Hbase is down
        else if (status == 1){
            // control if shortUrl has private access or other active optionals
            if(password.equals("d41d8cd98f00b204e9800998ecf8427e") && expireTime == (long) 0 && expireClicks == (long) 0){
                return 1;
            }
            return 0;
        }
        return 0;
    }


}
