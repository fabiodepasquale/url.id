import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static org.apache.logging.log4j.core.util.NameUtil.md5;


public class Statistics {
    static final Logger log = Logger.getLogger(ShortURL.class);

    public static void main(String[] args) throws IOException {


    }

    public static void addSEntry (String shortUrl, String ip, String countryCode, String browser) throws IOException{
        // Create connection config
        Configuration config = HBaseConfiguration.create();


        Connection conn = ConnectionFactory.createConnection();

        try {
            HBaseAdmin.checkHBaseAvailable(config);
        } catch (Exception e) {
            log.fatal("HBase is not running!");
            System.exit(0);
        }

        Table table = conn.getTable(TableName.valueOf("s"));

        //Generating timestamp
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp timestamp = new java.sql.Timestamp(now.getTime());

        // Instantiating Put class
        // accepts a row key.
        Put p = new Put(Bytes.toBytes(md5(shortUrl)+"_"+ timestamp));


        // adding values using addColumn() method
        // accepts column family name, qualifier/row name ,value
        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("su"), Bytes.toBytes(shortUrl));

        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("ip"), Bytes.toBytes(ip));

        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("cc"), Bytes.toBytes(countryCode));

        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("br"), Bytes.toBytes(browser));


        // Saving the put Instance to the Table.
        try {
            table.put(p);
        } catch (Exception e) {
            log.error("Something gone wrong. Data were not inserted.");
        }

        // closing Table
        table.close();

        conn.close();
    }
}
