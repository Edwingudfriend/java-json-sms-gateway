package com.opteral.gateway.database;

import com.opteral.gateway.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DAOUtil {

    private DAOUtil() {

    }


    public static Date toSqlDate(java.util.Date date) {
        return (date != null) ? new Date(date.getTime()) : null;
    }

    public static User fillUser(ResultSet resultSet) throws SQLException {

        User usuario = new User();

        usuario.setId(resultSet.getInt("id"));
        usuario.setName(resultSet.getString("name"));

        return usuario;
    }
}
