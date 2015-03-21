package com.opteral.gateway.database;

import com.opteral.gateway.GatewayException;
import com.opteral.gateway.model.SMS;

import java.sql.*;
import java.util.Date;

public class SMSDAOMySQL extends Database implements SMSDAO {


    @Override
    public void persist(SMS sms) throws GatewayException, SQLException {


        if (sms.getId() > 0)
            actualizar(sms);
        else
            guardar(sms);


    }

    private void guardar(SMS sms) throws GatewayException, SQLException {

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try
        {
            Date date = new Date();

            setConnection();
            conn.setAutoCommit(false);

            Timestamp timestampNow = new Timestamp(date.getTime());



            final String sql = "INSERT INTO sms (users_id, subid, msisdn, sender, text, status, ackurl, datetime_inbound, datetime_lastmodified, datetime_scheduled) VALUES" + "(?,?,?,?,?,?,?,?,?,?)";


            statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, sms.getUser_id());
            statement.setString(2,sms.getSubid());
            statement.setString(3,sms.getMsisdn());
            statement.setString(4,sms.getSender());
            statement.setString(5,sms.getText());
            statement.setInt(6, sms.getSms_status().getValue());
            statement.setString(7,sms.getAckurl());
            statement.setTimestamp(8, timestampNow);

            if (sms.getDatetimeScheduled() != null)
                statement.setTimestamp(9,new Timestamp(sms.getDatetimeScheduled().getTime()));
            else
                statement.setNull(9, Types.TIMESTAMP);


            statement.setTimestamp(10, timestampNow);



            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new GatewayException("");
            }

            resultSet =  statement.getGeneratedKeys();

            if (resultSet.next())
            {
                sms.setId(resultSet.getLong(1));
                conn.commit();

            }
            else
            {
                throw new GatewayException("");
            }



        }
        catch (Exception e) {
            throw new GatewayException("Error: Failed saving the SMS on database");
        }
        finally {
            conn.setAutoCommit(true);
            closeQuietly(conn, statement, resultSet);
        }
    }

    private void actualizar(SMS sms) throws SQLException, GatewayException {

        PreparedStatement statement = null;
        try
        {

            setConnection();
            conn.setAutoCommit(false);


            final String sql = "UPDATE sms SET sender  = ?, msisdn = ?, text = ?, datetime_scheduled = ?, subid = ?, ackurl = ?, idSMSC = ?, datetime_lastmodified = ? WHERE id = ? AND users_id = ?";


            statement = conn.prepareStatement(sql);

            statement.setString(1, sms.getSender());
            statement.setString(2, sms.getMsisdn());
            statement.setString(3, sms.getText());

            if (sms.getDatetimeScheduled() != null)
                statement.setTimestamp(4,new Timestamp(sms.getDatetimeScheduled().getTime()));
            else
                statement.setNull(4, Types.TIMESTAMP);

            statement.setString(5, sms.getSubid());
            statement.setString(6,sms.getAckurl());

            if (sms.getIdSMSC() != null && !sms.getIdSMSC().isEmpty())
                statement.setString(7, sms.getIdSMSC());
            else
                statement.setNull(7, Types.VARCHAR);

            statement.setTimestamp(8, new Timestamp(new Date().getTime()));

            statement.setLong(9, sms.getId());
            statement.setInt(10, sms.getUser_id());


            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new GatewayException("");
            }

            conn.commit();

        }
        catch (Exception e) {
            throw new GatewayException("Error: Failed updating SMS "+ sms.getId() +" on database");
        }
        finally {
            conn.setAutoCommit(true);
            closeQuietly(conn, statement, null);
        }
    }
    
}
