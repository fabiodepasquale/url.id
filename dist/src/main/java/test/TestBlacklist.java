package test;

import core.Blacklist;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;


public class TestBlacklist {

    @Test
    public void testAddBEntry() throws IOException {
        String word = "prova";
        boolean res = Blacklist.addBEntry(word);
        assertTrue(res);
    }
/*
    @Test
    public void testIsCensored() throws IOException {
        String word = "prova";
        List<String> bWords = new ArrayList<String>();
        bWords.add(word);
        boolean res = Blacklist.isCensored(word, bWords);
        assertTrue(res);
    }*/
}