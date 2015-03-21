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

    public static final int USER_ID = 456;
    public static final String USER_NAME = "amalio";



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


}