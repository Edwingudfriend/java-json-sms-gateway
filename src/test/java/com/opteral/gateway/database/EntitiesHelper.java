package com.opteral.gateway.database;

import com.opteral.gateway.TestHelper;
import com.opteral.gateway.Utilities;
import com.opteral.gateway.model.SMS;
import com.opteral.gateway.model.User;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EntitiesHelper {

    public static final long SMS_ID = 4654;
    public static final int USER_ID = 456;
    public static final String USER_NAME = "amalio";
    public static final String SENDER = "sender";
    public static final String MSISDN = "34656987415";
    public static final String TEXT = "The text of message with an ñ";
    public static final String SUBID = "subid1";
    public static final String ACKURL = "http://www.anurl.com/ack";
    public static final Timestamp DATETIME_SCHEDULED_2015 = new Timestamp(1451208600000L);
    public static final Timestamp DATETIME_SCHEDULED_2014 = new Timestamp(1419672600000L);


    private EntitiesHelper() {
        throw new UnsupportedOperationException("this class is a helper");
    }



    static void createTestTables(Connection connection) throws SQLException {

        String sql = Utilities.getFromFile("testTables.sql");
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
        sms.setDatetimeScheduled(DATETIME_SCHEDULED_2015);
        sms.setTest(false);

        return sms;
    }




}