import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.log4j.Logger;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

public class DatabaseInit {

    static final Logger log = Logger.getLogger(DatabaseInit.class);

    public static void main(String[] args) throws IOException {

        // Create connection config
        Configuration config = HBaseConfiguration.create();
        config.clear();
        config.setInt("timeout", 30);
        config.set("hbase.client.retries.number", "2");
        config.set("hbase.zookeeper.quorum", "localhost");
        config.set("hbase.zookeeper.property.clientPort", "2181");


        Connection conn = ConnectionFactory.createConnection();
        Admin admin = conn.getAdmin();  // get admin right

        try {
            HBaseAdmin.checkHBaseAvailable(config);
        } catch (Exception e) {
            log.fatal("HBase is not running!");
            System.exit(0);
        }

        // Create table : ShortUrl
        /* ------------------------------------------------------------- */
        TableName suTable = TableName.valueOf("su"); // stay for ShortUrl
        HTableDescriptor suTableDescriptor = new HTableDescriptor(suTable);
        suTableDescriptor.addFamily(new HColumnDescriptor("d")); // stay for data

        try {
            admin.createTable(suTableDescriptor);
        } catch (TableExistsException e) {
            log.error("ShortUrl table already exists");
        }


        // Create table : LongUrl
        /* ------------------------------------------------------------- */
        TableName luTable = TableName.valueOf("lu"); // stay for LongUrl
        HTableDescriptor luTableDescriptor = new HTableDescriptor(luTable);
        luTableDescriptor.addFamily(new HColumnDescriptor("d")); // stay for data

        try {
            admin.createTable(luTableDescriptor);
        } catch (TableExistsException e) {
            log.error("LongUrl table already exists");
        }

        // Create table : Statistics
        /* ------------------------------------------------------------- */
        TableName sTable = TableName.valueOf("s"); // stay for statistics
        HTableDescriptor sTableDescriptor = new HTableDescriptor(sTable);
        sTableDescriptor.addFamily(new HColumnDescriptor("d")); // stay for data

        try {
            admin.createTable(sTableDescriptor);
        } catch (TableExistsException e) {
            log.error("Statistics table already exists");
        }

        // Create table : Blacklist
        /* ------------------------------------------------------------- */
        TableName bTable = TableName.valueOf("b"); // stay for statistics
        HTableDescriptor bTableDescriptor = new HTableDescriptor(bTable);
        bTableDescriptor.addFamily(new HColumnDescriptor("w")); // stay for words

        try {
            admin.createTable(bTableDescriptor);
        } catch (TableExistsException e) {
            log.error("Blacklist table already exists");
        }

        conn.close();
		System.out.println("All done");
    }
}