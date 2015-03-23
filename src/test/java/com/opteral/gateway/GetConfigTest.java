package com.opteral.gateway;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GetConfigTest {

    @Test
    public  void testGetConfig() throws IOException {

        Config.MAX_SMS_SIZE = 0;
        Config.SUBID_MAX_SIZE = 0;
        Config.SENDER_MAX_SIZE = 0;

        Utilities.getConfig(true);

        assertEquals("87.222.103.149", Config.SMSC_IP);
        assertEquals("pavel", Config.SMSC_USERNAME);
        assertEquals("wpsd", Config.SMSC_PASSWORD);
        assertEquals(8056, Config.SMSC_PORT);

        assertEquals(140, Config.MAX_SMS_SIZE);
        assertEquals(11, Config.SENDER_MAX_SIZE);
        assertEquals(20, Config.SUBID_MAX_SIZE);
    }
}
