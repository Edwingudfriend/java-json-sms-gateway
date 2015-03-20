package com.opteral.gateway.database;

import java.sql.Date;

public final class DAOUtil {

    private DAOUtil() {

    }


    public static Date toSqlDate(java.util.Date date) {
        return (date != null) ? new Date(date.getTime()) : null;
    }
}
