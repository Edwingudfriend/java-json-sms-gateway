package com.opteral.gateway.json;

import com.google.gson.JsonSyntaxException;
import com.opteral.gateway.GatewayException;

public class InParser {
    public static RequestJSON getRequestJSON(String requestJSONString) throws GatewayException {

        RequestJSON requestJSON = null;
        try {
            requestJSON = GsonFactory.getGson().fromJson(requestJSONString, RequestJSON.class);

            if (requestJSON.getSms_request() == null || requestJSON.getSms_request().size() == 0)
                throw new GatewayException("Invalid JSON Request - sms requests are needed");
        } catch (JsonSyntaxException e) {
            throw new GatewayException("Invalid JSON Request");
        }

        return requestJSON;
    }

    public static ResponseJSON getResponseJSON (String gsonString) throws GatewayException {

        try
        {

            return  GsonFactory.getGson().fromJson(gsonString, ResponseJSON.class);

        }
        catch (Exception e)
        {
            throw new GatewayException("Invalid JSON Response");

        }

    }
}
