package com.opteral.gateway.json;

import com.google.gson.JsonSyntaxException;
import com.opteral.gateway.GatewayException;

public class InParser {
    public static RequestJSON getRequestJSON(String requestJSONString) throws GatewayException {

        RequestJSON requestJSON = null;
        try {
            requestJSON = GsonFactory.getGson().fromJson(requestJSONString, RequestJSON.class);
        } catch (JsonSyntaxException e) {
            throw new GatewayException("Invalid JSON Request");
        }

        return requestJSON;
    }
}
