package com.opteral.gateway.smsc;

import com.opteral.gateway.GatewayException;
import com.opteral.gateway.model.SMS;


public interface SMSC {
    public void sendSMS(SMS sms) throws GatewayException;
}
