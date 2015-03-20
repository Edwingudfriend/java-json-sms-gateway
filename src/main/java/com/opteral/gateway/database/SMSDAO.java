package com.opteral.gateway.database;

import com.opteral.gateway.GatewayException;
import com.opteral.gateway.model.SMS;

public interface SMSDAO {

    public void persist(SMS sms) throws GatewayException;

}
