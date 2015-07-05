package core;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDelete {

    public static void main(String[] args) throws IOException {
        truncateAll();
    }


    public static boolean truncateAll () throws IOException {

        List<String> nameTables = new ArrayList<>();
        nameTables.add("s");
        nameTables.add("su");
        nameTables.add("lu");
        nameTables.add("b");

        System.out.println("Do you really want to delete all data? (Y/N) ");
        char c = (char) System.in.read();

        if(c == 'Y' || c == 'y') {
            try {
                Connection conn = ConnectionFactory.createConnection();
                Admin admin = conn.getAdmin();  // get admin right

                if(!SystemConfig.checkConn()) {
                    return false;                   //  hbase is down
                }

                for(String string : nameTables ){
                    TableName tableName = TableName.valueOf(string);
                    admin.disableTable(tableName);// disabling table
                    admin.truncateTable(tableName, true); // Deleting
                }

                System.out.println("Database cleaned");

            } catch (IOException e) {
                return false;
            }

            return true;
        }
        else System.out.println("Nothing to do. Exit.");

        return false;
    }
}
