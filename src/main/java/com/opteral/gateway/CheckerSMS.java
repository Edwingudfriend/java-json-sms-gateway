package com.opteral.gateway;

import com.opteral.gateway.json.JSON_SMS;

import java.util.List;

public class CheckerSMS {

    public void check(List<JSON_SMS> listaJSONSMS) throws GatewayException {

        for (JSON_SMS sms : listaJSONSMS )
        {
            check(sms);
        }

    }

    public void check(JSON_SMS jsonsms) throws GatewayException {

        throw new GatewayException("not implemented");

    }
}
