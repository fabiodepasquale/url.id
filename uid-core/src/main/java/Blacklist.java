import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.FileOutputStream;
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

    public static void getBWords() throws IOException {

        // Create connection config
        Configuration config = HBaseConfiguration.create();

        Connection conn = ConnectionFactory.createConnection();

        try {
            HBaseAdmin.checkHBaseAvailable(config);
        } catch (Exception e) {
            log.fatal("HBase is not running!");
            System.exit(0);
        }

        // Instantiating Table class
        Table table = conn.getTable(TableName.valueOf("b"));

        // Instantiating the Scan class
        Scan scan = new Scan();

        // Scanning the required columns
        scan.addColumn(Bytes.toBytes("w"), Bytes.toBytes("w"));

        // Getting the scan result
        ResultScanner scanner = table.getScanner(scan);

        try {
            //Elemento radice
            Element root = new Element("words");
            //Documento
            Document document = new Document(root);

            // Reading values from scan result
            for (Result result = scanner.next(); result != null; result = scanner.next()){

                String stringa = new String(result.value(), "UTF-8");

                System.out.println("Found row : " + stringa);

                Element item = new Element("word");
                item.addContent(stringa);
                root.addContent(item);

            }



            //Creazione dell'oggetto XMLOutputter
            XMLOutputter outputter = new XMLOutputter();
            //Imposto il formato dell'outputter come "bel formato"
            outputter.setFormat(Format.getPrettyFormat());
            //Produco l'output sul file xml.foo
            outputter.output(document, new FileOutputStream("foo.xml"));
            System.out.println("File creato:");
            //Stampo l'output anche sullo standard output
            outputter.output(document, System.out);
        }
        catch (IOException e) {
            System.err.println("Errore durante il parsing del documento");
            e.printStackTrace();
        }


        //closing the scanner
        scanner.close();
    }




}
