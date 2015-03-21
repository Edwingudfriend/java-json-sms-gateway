package com.opteral.gateway.database;

import com.opteral.gateway.GatewayException;
import com.opteral.gateway.LoginException;
import com.opteral.gateway.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.opteral.gateway.Utilities.findMD5;
import static com.opteral.gateway.database.DAOUtil.fillUser;

public class UserDAOMySQL extends Database implements UserDAO {
    @Override
    public User identify(String name, String password) throws GatewayException {


        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            password = findMD5(password);
            setConnection();

            String sql = "SELECT * FROM users WHERE name=? and passwd=?";


            statement = conn.prepareStatement(sql);

            statement.setString(1, name);
            statement.setString(2, password);

            resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst() ) {

                throw new LoginException("Authentication error");
            }

            resultSet.next();

            return fillUser(resultSet);

        }
        catch (Exception e) {
            throw new LoginException("Authentication error");
        }
        finally {
            closeQuietly(conn, statement, resultSet);
        }

    }
}
