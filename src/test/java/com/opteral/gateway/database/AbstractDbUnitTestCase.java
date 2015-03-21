package com.opteral.gateway.database;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbConnection;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class AbstractDbUnitTestCase {

    protected static Connection connection;
    protected static HsqldbConnection dbunitConnection;


    protected static UserDAOMySQL userDAO = new UserDAOMySQL();
    protected static SMSDAOMySQL smsDAO = new SMSDAOMySQL();

    @BeforeClass
    public static void setupDatabase() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        connection = DriverManager.getConnection("jdbc:hsqldb:mem:my-project-test;shutdown=true");
        dbunitConnection = new HsqldbConnection(connection,null);
        userDAO.setConnection(connection);
        smsDAO.setConnection(connection);
        EntitiesHelper.createTestTables(connection);
    }

    @AfterClass
    public static void closeDatabase() throws Exception {
        if ( dbunitConnection != null ) {
            dbunitConnection.close();
            dbunitConnection = null;
        }
    }

    public static IDataSet getDataSet(String name) throws Exception {
        InputStream inputStream = AbstractDbUnitTestCase.class.getResourceAsStream(name);
        assertNotNull("file " + name + " not found in classpath", inputStream );
        Reader reader = new InputStreamReader(inputStream);
        FlatXmlDataSet dataset = new FlatXmlDataSet(reader);
        return dataset;
    }

    public static IDataSet getReplacedDataSet(String name, long id) throws Exception {
        IDataSet originalDataSet = getDataSet(name);
        return getReplacedDataSet(originalDataSet, id);
    }

    public static IDataSet getReplacedDataSet(IDataSet originalDataSet, long id) throws Exception {
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(originalDataSet);
        replacementDataSet.addReplacementObject("[ID]", id);
        replacementDataSet.addReplacementObject("[NULL]", null);
        replacementDataSet.addReplacementObject("[create_date]", new Timestamp(new Date().getTime()));
        return replacementDataSet;
    }

}
