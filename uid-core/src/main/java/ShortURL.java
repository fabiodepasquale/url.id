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


public class ShortURL {
    static final Logger log = Logger.getLogger(ShortURL.class);

    public static void main(String[] args) throws IOException {


    }

    public static void addSUEntry (String shortUrl, String longUrl, boolean protection, String password,  int expireTime, int expireClick) throws IOException{
        // Create connection config
        Configuration config = HBaseConfiguration.create();


        Connection conn = ConnectionFactory.createConnection();

        try {
            HBaseAdmin.checkHBaseAvailable(config);
        } catch (Exception e) {
            log.fatal("HBase is not running!");
            System.exit(0);
        }


        Table table = conn.getTable(TableName.valueOf("su"));

        //Generating timestamp
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp timestamp = new java.sql.Timestamp(now.getTime());

        // Instantiating Put class
        // accepts a row key.
        Put p = new Put(Bytes.toBytes(md5(shortUrl)+"_"+ timestamp));


        String pageTitle = "Inizializzare";
        String clicks = "Inizializzare";
        String status = "Inizializzare";
        String createdTime = "Inizializzare";


        // adding values using addColumn() method
        // accepts column family name, qualifier/row name ,value
        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("su"), Bytes.toBytes(shortUrl));

        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("lu"), Bytes.toBytes(longUrl));

        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("pr"), Bytes.toBytes(protection));

        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("pw"), Bytes.toBytes(password));

        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("et"), Bytes.toBytes(expireTime));

        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("ec"), Bytes.toBytes(expireClick));

        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("pt"), Bytes.toBytes(pageTitle));

        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("cl"), Bytes.toBytes(clicks));

        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("st"), Bytes.toBytes(status));

        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("ct"), Bytes.toBytes(createdTime));



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
