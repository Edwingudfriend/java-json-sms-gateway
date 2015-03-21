package com.opteral.gateway.database;

import com.opteral.gateway.GatewayException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Database {

    protected Connection conn;


    protected void setConnection(Connection connection)
    {
        this.conn = connection;
    }

    protected void setConnection() throws GatewayException {
        if (!connectionIsTest())
            this.conn = SingletonDataSource.getConnection();
    }

    protected void closeQuietly(Connection connection, Statement statement, ResultSet resultSet) {

        if (resultSet != null) try { resultSet.close(); } catch (SQLException logOrIgnore) {}
        if (statement != null) try { statement.close(); } catch (SQLException logOrIgnore) {}
        if (!connectionIsTest())
            if (connection != null) try { connection.close(); } catch (SQLException logOrIgnore) {}

    }

    private boolean connectionIsTest() {

        if (conn != null && conn.getClass() == org.hsqldb.jdbc.JDBCConnection.class)
            return true;
        else
            return false;
    }


}