package core;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.logging.log4j.core.util.NameUtil.md5;


public class LongURL {
    private static org.apache.log4j.Logger log = Logger.getLogger(LongURL.class);
    public List<String> shortUrls = null;
    public String shortUrl = null;

    public static boolean addLUEntry (String longUrl, String shortUrl, long timestamp) throws IOException{

        Connection conn = ConnectionFactory.createConnection();

        if(!SystemConfig.checkConn()) {
            return false;                   //  hbase is down
        }

        Table table = conn.getTable(TableName.valueOf("lu"));

        // md5(longUrl)_timestamp as RowKey.
        Put p = new Put(Bytes.toBytes(md5(longUrl)+"_"+ timestamp));

        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("su"), Bytes.toBytes(shortUrl));

        // Saving the Put instance to the table.
        table.put(p);

        table.close();
        conn.close();
        return true;
    }


    /**
     * Return te list of short Url connect to long url
     *
     * @throws IOException
     */
    public boolean getShortUrlsList(String longUrl) throws IOException {

        Connection conn = ConnectionFactory.createConnection();

        if(!SystemConfig.checkConn()) {
            return false;                   //  hbase is down
        }

        Table table = conn.getTable(TableName.valueOf("lu"));

        Scan scan = new Scan();
        scan.addColumn(Bytes.toBytes("d"), Bytes.toBytes("su"));

        //Set the start row
        scan.setStartRow(Bytes.toBytes(md5(longUrl) + "_" + 0));
        //Set the end row
        scan.setStopRow(Bytes.toBytes(md5(longUrl) + "_" + Timestamp.getTimestamp()));

        // Getting the scan result
        ResultScanner scanner = table.getScanner(scan);

        this.shortUrls = new ArrayList<>();

        // Reading values from scan result
        for (Result result = scanner.next(); result != null; result = scanner.next()){
            String string = new String(result.value(), "UTF-8");
            this.shortUrls.add(string);
        }

        scanner.close();
        table.close();
        conn.close();

        return true;
    }

    /**
     * @implNote Search for already existent public shortUrl
     * @throws IOException
     * @return -1 if HBase is down; 0 if any public shortUrl wasn't found;
     *          in success case fills up shortUrl class variable
     */
    public int searchPublicShortUrl(String longUrl) throws IOException {

        if(!getShortUrlsList(longUrl)) {
            return -1;          // HBase is down
        }

        ShortURL su = new ShortURL();

        //look for a good old shortUrl
        for (String url : this.shortUrls) {
            shortUrl = url;

            if (su.canBeReused(shortUrl) == 1) {
                return 1;       // public shortUrl found
            } else {
                shortUrl = null;
            }
        }

        return 0;   // not found
    }
}
