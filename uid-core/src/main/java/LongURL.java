import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.io.IOException;

import static org.apache.logging.log4j.core.util.NameUtil.md5;


public class LongURL {
    static final Logger log = Logger.getLogger(LongURL.class);

    public static void main(String[] args) throws IOException {


    }

    public static void addLUEntry (String longUrl, String shortUrl, long timestamp) throws IOException{
        // Create connection config
        Configuration config = HBaseConfiguration.create();


        Connection conn = ConnectionFactory.createConnection();

        try {
            HBaseAdmin.checkHBaseAvailable(config);
        } catch (Exception e) {
            log.fatal("HBase is not running!");
            System.exit(0);
        }


        Table table = conn.getTable(TableName.valueOf("lu"));

        // Instantiating Put class
        // accepts a row key.
        Put p = new Put(Bytes.toBytes(md5(longUrl)+"_"+ timestamp));


        // adding values using addColumn() method
        // accepts column family name, qualifier/row name ,value
        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("su"), Bytes.toBytes(shortUrl));

        p.addColumn(Bytes.toBytes("d"),
                Bytes.toBytes("ts"), Bytes.toBytes(timestamp));


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
