package com.opteral.gateway.database;

import com.opteral.gateway.GatewayException;
import com.opteral.gateway.model.ACK;
import com.opteral.gateway.model.SMS;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface SMSDAO {

    public void persist(SMS sms) throws GatewayException, SQLException;
    public List<SMS> getSMSForSend(Date aFecha) throws GatewayException;
    public void updateSMS_Status(ACK ack) throws GatewayException, SQLException;

}
