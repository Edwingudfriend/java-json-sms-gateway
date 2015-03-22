package com.opteral.gateway.smsc;

import com.opteral.gateway.GatewayException;
import com.opteral.gateway.model.SMS;

import java.io.IOException;


public interface SMSC {
    public void sendSMS(SMS sms) throws GatewayException, IOException;
}
