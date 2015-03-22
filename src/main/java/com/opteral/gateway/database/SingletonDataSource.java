package com.opteral.gateway.database;

import com.opteral.gateway.GatewayException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public enum SingletonDataSource {

    INSTANCE;

    private final DataSource dataSource;

    SingletonDataSource() throws ExceptionInInitializerError {

        Context initContext = null;

        try {
            initContext = new InitialContext();

            this.dataSource = (DataSource) initContext.lookup("java:jboss/datasources/jjsgDS");
        } catch (NamingException e) {
            throw new ExceptionInInitializerError();
        }
    }

    // Static getter
    public static Connection getConnection() throws GatewayException {
        try {
            return INSTANCE.dataSource.getConnection();
        } catch (SQLException e) {
            throw new GatewayException("There are not free connection to the database");
        }
    }

}