package com.opteral.gateway.database;

import com.opteral.gateway.model.SMS;
import com.opteral.gateway.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public final class DAOUtil {

    private DAOUtil() {

    }


    public static Date toSqlDate(java.util.Date date) {
        return (date != null) ? new Date(date.getTime()) : null;
    }

    public static Timestamp toSqlTimeStamp(java.util.Date date) {
        return (date != null) ? new Timestamp(date.getTime()) : null;
    }

    public static User fillUser(ResultSet resultSet) throws SQLException {

        User usuario = new User();

        usuario.setId(resultSet.getInt("id"));
        usuario.setName(resultSet.getString("name"));

        return usuario;
    }

    public static SMS fillSMS(ResultSet resultSet) throws SQLException {

        SMS sms = new SMS();
        sms.setId(resultSet.getLong("id"));
        sms.setUser_id(resultSet.getInt("users_id"));
        sms.setMsisdn(resultSet.getString("msisdn"));
        sms.setSender(resultSet.getString("sender"));
        sms.setText(resultSet.getString("text"));
        sms.setDatetimeScheduled(resultSet.getTimestamp("datetime_scheduled"));
        sms.setTest(false);
        sms.setAckurl(resultSet.getString("ackurl"));
        sms.setSubid(resultSet.getString("subid"));
        sms.setSms_status(SMS.SMS_Status.fromInt(resultSet.getInt("status")));


        return sms;

    }
}
