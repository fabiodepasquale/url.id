package core;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.log4j.Logger;

public class SystemConfig {
    private static org.apache.log4j.Logger log = Logger.getLogger(SystemConfig.class);

    public static String domain = "http://url.id/";


    public static boolean checkConn() {
        Configuration config = HBaseConfiguration.create();

        try {
            HBaseAdmin.checkHBaseAvailable(config);
            return true;
        }
        catch (ZooKeeperConnectionException zoo) {
            return false;
        }
        catch (MasterNotRunningException master) {
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }
}


