package com.opteral.gateway.json;

public class OutParser {

    public static String getJSON (ResponseJSON responseJSON)  {

        try
        {
            return  GsonFactory.getGson().toJson(responseJSON);

        }
        catch (Exception e)
        {
            return "Respuesta con formato incorrecto";

        }

    }
}
