package com.opteral.gateway.database;

import com.opteral.gateway.GatewayException;
import com.opteral.gateway.model.SMS;

import java.sql.SQLException;

public interface SMSDAO {

    public void persist(SMS sms) throws GatewayException, SQLException;

}
