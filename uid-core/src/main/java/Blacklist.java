import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.io.IOException;

import static org.apache.logging.log4j.core.util.NameUtil.md5;


public class Blacklist {
    static final Logger log = Logger.getLogger(Blacklist.class);

    public static void main(String[] args) throws IOException {
    }

    public static void addBEntry (String word) throws IOException{
        // Create connection config
        Configuration config = HBaseConfiguration.create();

        Connection conn = ConnectionFactory.createConnection();

        try {
            HBaseAdmin.checkHBaseAvailable(config);
        } catch (Exception e) {
            log.fatal("HBase is not running!");
            System.exit(0);
        }

        Table table = conn.getTable(TableName.valueOf("b"));

        // Instantiating Put class
        // accepts a row key.
        Put p = new Put(Bytes.toBytes(md5(word)));


        // adding values using addColumn() method
        // accepts column family name, qualifier/row name ,value
        p.addColumn(Bytes.toBytes("w"),
                Bytes.toBytes("w"), Bytes.toBytes(word));


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
