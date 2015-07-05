package core;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.logging.log4j.core.util.NameUtil.md5;

public class Statistics {

    public Map<String, Integer> countryCode = null;
    public Map<String, Integer> browser = null;
    public Map<String, Integer> os = null;

    /**
     * @implNote Add new row to statistics table
     * @throws IOException
     * @return "false" if HBase is down; "true" if entry was inserted to DB
     */
    public static boolean addSEntry (String shortUrl, String ip, String countryCode, String browser, String os) throws IOException{

        Connection conn = ConnectionFactory.createConnection();

        if(!SystemConfig.checkConn()) {
            return false;                   //  HBase is down
        }

        Table table = conn.getTable(TableName.valueOf("s"));

        long t = Timestamp.getTimestamp();

        // md5(shortUrl)_CurrentTimestamp as RowKey
        Put p = new Put(Bytes.toBytes(md5(shortUrl)+"_"+ t));

        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("ip"), Bytes.toBytes(ip));
        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("cc"), Bytes.toBytes(countryCode));
        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("br"), Bytes.toBytes(browser));
        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("os"), Bytes.toBytes(os));
        p.addColumn(Bytes.toBytes("d"), Bytes.toBytes("t"), Bytes.toBytes(t));

        // Save the Put instance to the table.
        table.put(p);

        table.close();
        conn.close();

        return true;
    }


    /**
     * @implNote Give statistics for requested shortUrl
     * @throws IOException
     * @return -1 if HBase is down, 0 if shortUrl wasn't found, 1 if all done successfully;
     *         in success case fills up class variables.
     */
    public int getStats(String shortUrl, long fromTime, long toTime) throws IOException {

        ShortURL su = new ShortURL();

        int exist = su.existShortUrl(shortUrl);

        //if shortUrl exist
        if(exist == 1){
            // try to get countries stats
            if (!getSingleStat(shortUrl, fromTime, toTime, "cc")) {
                return -1;      // HBase is down
            }
            // try to get browsers stats
            if (!getSingleStat(shortUrl, fromTime, toTime, "br")) {
                return -1;      // HBase is down
            }
            // try to get OS stats
            if (!getSingleStat(shortUrl, fromTime, toTime, "os")) {
                return -1;      // HBase is down
            }
            return 1;   // All done successfully
        }
        else if (exist == 0){
            return 0;   // shortUrl not found
        }
        return -1;  // HBase is down
    }


    /**
     * @implNote Give single statistic (countries/browsers/os) for requested shortUrl and period;
     *           Existence of the shortUrl must be control before.
     * @return false if HBase is down, true if statistics were successfully received.
     * @throws IOException
     */
    private boolean getSingleStat(String shortUrl, long fromTime, long toTime, String columnName) throws IOException {

        Connection conn = ConnectionFactory.createConnection();

        if (!SystemConfig.checkConn()) {
            return false;                   //  HBase is down
        }

        Table table = conn.getTable(TableName.valueOf("s"));
        Scan scan = new Scan();

        // Scan the required columns
        scan.addFamily(Bytes.toBytes("d"));
        scan.addColumn(Bytes.toBytes("d"), Bytes.toBytes(columnName));

        scan.setStartRow(Bytes.toBytes(md5(shortUrl) + "_" + fromTime));
        scan.setStopRow(Bytes.toBytes(md5(shortUrl) + "_" + toTime));

        // Get the scan result
        ResultScanner scanner = table.getScanner(scan);

        // Create map: {value : occurrence number}
        Map<String, Integer> stat = new HashMap<>();

        // Read values from scan result
        for (Result result = scanner.next(); result != null; result = scanner.next()) {

            String string = new String(result.value(), "UTF-8");

            //if stat exist in Map increase his number else create it
            if (stat.containsKey(string)) {
                Integer val = stat.get(string);
                stat.put(string, val + 1);
            } else
                stat.put(string, 1);
        }

        scanner.close();
        table.close();
        conn.close();

        //use columnName to set the stat variabile: country, browser or os
        switch (columnName) {
            case "cc":    //country
                this.countryCode = stat;
                break;
            case "br":  //browser
                this.browser = stat;
                break;
            default:    //os
                this.os = stat;
                break;
        }

        return true;
    }


}
