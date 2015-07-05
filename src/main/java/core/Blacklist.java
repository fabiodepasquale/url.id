package core;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.apache.logging.log4j.core.util.NameUtil.md5;

public class Blacklist {
    private static org.apache.log4j.Logger log = Logger.getLogger(Blacklist.class);

    public List<String> bWords = null;

    public static boolean addBEntry(String word) throws IOException{

        if (!SystemConfig.checkConn()) {
            return false;
        }

        Connection conn = ConnectionFactory.createConnection();

        Table table = conn.getTable(TableName.valueOf("b"));

        // md5(word) as RowKey
        Put p = new Put(Bytes.toBytes(md5(word)));

        p.addColumn(Bytes.toBytes("w"), Bytes.toBytes("w"), Bytes.toBytes(word));

        // Save the Put instance to the table.
        table.put(p);

        table.close();
        conn.close();
        return true;
    }

    public boolean getBWords() throws IOException {

        if (!SystemConfig.checkConn()) {
            return false;
        }

        Connection conn = ConnectionFactory.createConnection();

        Table table = conn.getTable(TableName.valueOf("b"));
        Scan scan = new Scan();

        // Scan the required columns
        scan.addColumn(Bytes.toBytes("w"), Bytes.toBytes("w"));

        // Get the scan result
        ResultScanner scanner = table.getScanner(scan);

        this.bWords = new ArrayList<>();

        // Read values from scan result
        for (Result result = scanner.next(); result != null; result = scanner.next()){
            String word = new String(result.value(), "UTF-8");
            this.bWords.add(word);
        }

        scanner.close();
        table.close();
        conn.close();
        return true;
    }

    public boolean isCensored(String word, List<String> bWords) throws IOException {

        for (String temp : bWords) {
            if(word.toLowerCase().contains(temp.toLowerCase())){
                return true;
            }
        }

        return false;
    }
}
