package core;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class DatabaseInit {

    public static boolean main(String[] args) throws IOException {

        Connection conn = ConnectionFactory.createConnection();
        Admin admin = conn.getAdmin();  // get admin right

        if(!SystemConfig.checkConn()) {
            return false;                   //  hbase is down
        }

        // Create table : ShortUrl
        /* ------------------------------------------------------------- */
        TableName suTable = TableName.valueOf("su"); // stay for ShortUrl
        HTableDescriptor suTableDescriptor = new HTableDescriptor(suTable);
        suTableDescriptor.addFamily(new HColumnDescriptor("d")); // stay for data

        try {
            admin.createTable(suTableDescriptor);
        } catch (TableExistsException e) {
            System.out.println("ShortUrl table already exists");
        }

        // Create table : LongUrl
        /* ------------------------------------------------------------- */
        TableName luTable = TableName.valueOf("lu"); // stay for LongUrl
        HTableDescriptor luTableDescriptor = new HTableDescriptor(luTable);
        luTableDescriptor.addFamily(new HColumnDescriptor("d")); // stay for data

        try {
            admin.createTable(luTableDescriptor);
        } catch (TableExistsException e) {
            System.out.println("LongUrl table already exists");
        }

        // Create table : Statistics
        /* ------------------------------------------------------------- */
        TableName sTable = TableName.valueOf("s"); // stay for statistics
        HTableDescriptor sTableDescriptor = new HTableDescriptor(sTable);
        sTableDescriptor.addFamily(new HColumnDescriptor("d")); // stay for data

        try {
            admin.createTable(sTableDescriptor);
        } catch (TableExistsException e) {
            System.out.println("Statistics table already exists");
        }

        // Create table : Blacklist
        /* ------------------------------------------------------------- */
        TableName bTable = TableName.valueOf("b"); // stay for statistics
        HTableDescriptor bTableDescriptor = new HTableDescriptor(bTable);
        bTableDescriptor.addFamily(new HColumnDescriptor("w")); // stay for words

        try {
            admin.createTable(bTableDescriptor);
        } catch (TableExistsException e) {
            System.out.println("Blacklist table already exists");
        }

        conn.close();
		System.out.println("All done");
        return true;
    }
}