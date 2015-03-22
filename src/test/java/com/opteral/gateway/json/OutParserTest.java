package com.opteral.gateway.json;

import com.opteral.gateway.GatewayException;
import com.opteral.gateway.model.SMS;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OutParserTest {

    private ResponseJSON respuestaJSON;

    @Before
    public void setUp()
    {
        SMS_Response sms_response1 = new SMS_Response();
        SMS_Response sms_response2 = new SMS_Response();
        SMS_Response sms_response3 = new SMS_Response();
        SMS_Response sms_response4 = new SMS_Response();

        sms_response1.setRequest_ok(true);
        sms_response2.setRequest_ok(true);
        sms_response3.setRequest_ok(true);
        sms_response4.setRequest_ok(true);

        sms_response1.setId(400);
        sms_response2.setId(500);
        sms_response3.setId(500);
        sms_response4.setId(400);

        sms_response1.setSubid("subid1");
        sms_response2.setSubid("subid2");
        sms_response3.setSubid("subid2");
        sms_response4.setSubid("subid3");

        sms_response1.setStatus(SMS.SMS_Status.ACCEPTD);
        sms_response2.setStatus(SMS.SMS_Status.PROGRAMED);
        sms_response3.setStatus(SMS.SMS_Status.PROGRAMED);
        sms_response4.setStatus(SMS.SMS_Status.ACCEPTD);

        List<SMS_Response> sms_responses = new ArrayList<SMS_Response>();
        sms_responses.add(sms_response1);
        sms_responses.add(sms_response2);
        sms_responses.add(sms_response3);
        sms_responses.add(sms_response4);

        respuestaJSON = new ResponseJSON(ResponseJSON.ResponseCode.OK);
        respuestaJSON.setSms_responses(sms_responses);


    }

    @Test
    public void outParseRespuestaOk() throws GatewayException {

        String cadena = OutParser.getJSON(respuestaJSON);

        ResponseJSON respuestaJSON_reSerializada = InParser.getResponseJSON(cadena);

        assertEquals(ResponseJSON.ResponseCode.OK,  respuestaJSON_reSerializada.getResponse_code());

        assertTrue(respuestaJSON_reSerializada.getSms_responses().get(0).isRequest_ok());
        assertTrue(respuestaJSON_reSerializada.getSms_responses().get(1).isRequest_ok());
        assertTrue(respuestaJSON_reSerializada.getSms_responses().get(2).isRequest_ok());
        assertTrue(respuestaJSON_reSerializada.getSms_responses().get(3).isRequest_ok());

        assertEquals(400, respuestaJSON_reSerializada.getSms_responses().get(0).getId());
        assertEquals(500, respuestaJSON_reSerializada.getSms_responses().get(1).getId());
        assertEquals(500, respuestaJSON_reSerializada.getSms_responses().get(2).getId());
        assertEquals(400, respuestaJSON_reSerializada.getSms_responses().get(3).getId());

        assertEquals("subid1", respuestaJSON_reSerializada.getSms_responses().get(0).getSubid());
        assertEquals("subid2", respuestaJSON_reSerializada.getSms_responses().get(1).getSubid());
        assertEquals("subid2", respuestaJSON_reSerializada.getSms_responses().get(2).getSubid());
        assertEquals("subid3", respuestaJSON_reSerializada.getSms_responses().get(3).getSubid());

        assertEquals(SMS.SMS_Status.ACCEPTD, respuestaJSON_reSerializada.getSms_responses().get(0).getStatus());
        assertEquals(SMS.SMS_Status.PROGRAMED, respuestaJSON_reSerializada.getSms_responses().get(1).getStatus());
        assertEquals(SMS.SMS_Status.PROGRAMED, respuestaJSON_reSerializada.getSms_responses().get(2).getStatus());
        assertEquals(SMS.SMS_Status.ACCEPTD, respuestaJSON_reSerializada.getSms_responses().get(3).getStatus());
    }

}