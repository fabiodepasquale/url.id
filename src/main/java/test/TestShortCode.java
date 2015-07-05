package test;

import core.*;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;


public class TestShortCode {

    @Test
    public void testGetShortCode() throws IOException {
        int length = 5;
        ShortCode sc = new ShortCode();
        boolean res = sc.getShortCode(length);
        assertTrue(res);
    }
}
