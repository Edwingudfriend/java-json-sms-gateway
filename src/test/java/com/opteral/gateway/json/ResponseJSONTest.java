package com.opteral.gateway.json;

import com.opteral.gateway.GatewayException;
import com.opteral.gateway.LoginException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResponseJSONTest {

    private String msg = "foo msg";

    @Test
    public void testExceptionRespuesta()
    {
        ResponseJSON responseJSON = new ResponseJSON(new GatewayException(msg));

        assertEquals(ResponseJSON.ResponseCode.ERROR_GENERAL, responseJSON.getResponse_code());
        assertEquals(msg, responseJSON.getMsg());
    }

    @Test
    public void testLoginExceptionRespuesta()
    {
        ResponseJSON responseJSON = new ResponseJSON(new LoginException(msg));

        assertEquals(ResponseJSON.ResponseCode.ERROR_LOGIN, responseJSON.getResponse_code());
        assertEquals(msg, responseJSON.getMsg());
    }

}
