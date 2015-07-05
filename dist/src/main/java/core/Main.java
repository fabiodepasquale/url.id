package core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {
 //   private static org.apache.log4j.Logger log = Logger.getLogger(Main.class);

    private static final Map<String, Class<?>> ENTRY_POINTS =
            new HashMap<String, Class<?>>();
    static{
        ENTRY_POINTS.put("-a", api.Application.class);
        ENTRY_POINTS.put("-p", Prova.class);
        ENTRY_POINTS.put("-i", DatabaseInit.class);
        ENTRY_POINTS.put("-d", DatabaseDelete.class);
    }

    public static void main(final String[] args) throws Exception{
        try {
            checkParams(ENTRY_POINTS, args);

            final String[] argsCopy =
                    args.length > 1
                            ? Arrays.copyOfRange(args, 1, args.length)
                            : new String[0];
            ENTRY_POINTS.get(args[0]).getMethod("main", String[].class).invoke(null,
                    (Object) argsCopy);

        } catch (UIDException e) {
            help();
        }
    }


    private static void checkParams(Map ENTRY_POINTS, String[] args) throws UIDException {
        if(args.length == 0) {
            throw new UIDException("Not enough arguments");
        }

        else if(ENTRY_POINTS.get(args[0])==null){
            throw new UIDException("Entry point doesn't exist");
        }
    }

    public static void help() {
        System.out.println("Url Shortener <URL.id> Server");
        System.out.println("Release 1.0 Beta\n");
        System.out.println("Usage: java -jar uid-1.0.jar [options]");
        System.out.println("Options:");
        System.out.println("  -i \t initialize database");
        System.out.println("  -a \t start API server");
        System.out.println("  -d \t delete all data in the database");
        System.out.println("  -p \t start class Prova");
    }


}
