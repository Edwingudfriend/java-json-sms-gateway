package com.opteral.gateway.database;

import com.opteral.gateway.GatewayException;
import com.opteral.gateway.model.ACK;
import com.opteral.gateway.model.SMS;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.opteral.gateway.database.DAOUtil.fillSMS;

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
            statement.setTimestamp(9, timestampNow);

            if (sms.getDatetimeScheduled() != null)
                statement.setTimestamp(10,new Timestamp(sms.getDatetimeScheduled().getTime()));
            else
                statement.setNull(10, Types.TIMESTAMP);


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


    @Override
    public List<SMS> getSMSForSend(java.sql.Date aFecha) throws GatewayException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        ArrayList<SMS> listaSMS = new ArrayList<SMS>();

        try {

            setConnection();

            String sql = "SELECT * FROM sms WHERE status < ? AND (datetime_scheduled <= ? OR datetime_scheduled is NULL )";

            statement = conn.prepareStatement(sql);

            statement.setInt(1, SMS.SMS_Status.ONSMSC.getValue());
            statement.setDate(2, aFecha);

            resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst() ) {

                return listaSMS;
            }


            while (resultSet.next()) {

                SMS sms = fillSMS(resultSet);

                listaSMS.add(sms);


            }

            return listaSMS;


        }
        catch (Exception e) {
            throw new GatewayException ("Error: Failed recovering sms for send");
        }
        finally
        {
            closeQuietly(conn, statement, resultSet);
        }
    }


    @Override
    public void updateSMS_Status(ACK ack) throws GatewayException, SQLException {

        PreparedStatement statement = null;

        try
        {
            setConnection();
            conn.setAutoCommit(false);


            String sql;

            if (ack.getIdSMS() != null)
                sql = "UPDATE sms SET status = ?, datetime_lastmodified = ? WHERE id = ?";
            else
                sql =  "UPDATE sms SET status = ?, datetime_lastmodified = ? WHERE idSMSC = ?";


            statement = conn.prepareStatement(sql);

            statement.setInt(1, ack.getSms_status().getValue());
            statement.setTimestamp(2, new Timestamp(ack.getAcktimestamp().getTime()));


            if (ack.getIdSMS() != null)
                statement.setLong(3, ack.getIdSMS());
            else
                statement.setString(3, ack.getIdSMSC());

            statement.executeUpdate();

            conn.commit();


        }
        catch (Exception e)
        {
            throw new GatewayException ("Error: Failed updating sms_status on sms with id "+ ack.getIdSMS() +" and with idSMSC"+ack.getIdSMSC() + " on database");
        }
        finally {
            conn.setAutoCommit(true);
            closeQuietly(conn, statement, null);
        }

    }

    @Override
    public SMS getSMS(long id) throws GatewayException {

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        SMS sms = new SMS();

        try {

            setConnection();

            String sql = "SELECT * FROM sms WHERE id = ?";

            statement = conn.prepareStatement(sql);

            statement.setLong(1, id);

            resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst() ) {

                return null;
            }


            resultSet.next();

            sms = fillSMS(resultSet);

            return sms;


        }
        catch (Exception e) {
            throw new GatewayException ("Error: Failed recovering sms");
        }
        finally
        {
            closeQuietly(conn, statement, resultSet);
        }
    }

}
