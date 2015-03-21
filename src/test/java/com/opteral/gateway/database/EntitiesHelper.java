package com.opteral.gateway.database;

import com.opteral.gateway.TestHelper;
import com.opteral.gateway.model.SMS;
import com.opteral.gateway.model.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EntitiesHelper {

    public static final int SMS_ID = 4654;
    public static final int USER_ID = 456;
    public static final String USER_NAME = "amalio";
    private static final String SENDER = "sender";
    private static final String MSISDN = "34656987415";
    private static final String TEXT = "The text of message with an ñ";
    private static final String SUBID = "subid1";
    private static final String ACKURL = "http://www.anurl.com/ack";
    private static final Date DATETIME_SCHEDULED = new Date(1451208600000L);


    private EntitiesHelper() {
        throw new UnsupportedOperationException("this class is a helper");
    }



    static void createTestTables(Connection connection) throws SQLException {

        String sql = TestHelper.getFromFile("testTables.sql");
        Statement stmt = connection.createStatement();
        try {
            stmt.execute(sql);
        } finally {
            stmt.close();
        }
    }


    public static void assertUser(User user) {

        assertNotNull(user);
        assertEquals(USER_ID, user.getId());
        assertEquals(USER_NAME, user.getName());


    }

    public static SMS newSMS() {

        SMS sms = new SMS();

        sms.setId(SMS_ID);
        sms.setUser_id(USER_ID);
        sms.setSender(SENDER);
        sms.setMsisdn(MSISDN);
        sms.setText(TEXT);
        sms.setSubid(SUBID);
        sms.setAckurl(ACKURL);
        sms.setDatetimeScheduled(DATETIME_SCHEDULED);
        sms.setTest(false);

        return sms;
    }

    public static void assertSMS(SMS sms) {

        assertNotNull(sms);
        assertEquals(SMS_ID, sms.getId());
        assertEquals(USER_ID, sms.getUser_id());
        assertEquals(SENDER, sms.getSender());
        assertEquals(MSISDN, sms.getMsisdn());
        assertEquals(TEXT, sms.getText());
        assertEquals(SUBID, sms.getSubid());
        assertEquals(ACKURL, sms.getAckurl());
        assertEquals(DATETIME_SCHEDULED, sms.getDatetimeScheduled());
        assertEquals(true, sms.isTest());

    }


}