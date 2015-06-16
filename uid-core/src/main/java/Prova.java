import java.io.IOException;

public class Prova {

    public static void main(String[] args) throws IOException {

        Blacklist b = new Blacklist();
        b.addBEntry("Sesso");

        b.addBEntry("Pipì");

        b.addBEntry("Cazzo");

        b.addBEntry("Merda");

        b.addBEntry("Cacca");

        b.getBWords();

    }


}