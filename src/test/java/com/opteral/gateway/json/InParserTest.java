package com.opteral.gateway.json;

import com.opteral.gateway.GatewayException;
import com.opteral.gateway.TestHelper;
import com.opteral.gateway.Utilities;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InParserTest {



    @Test (expected = GatewayException.class)
    public void notAJSON() throws GatewayException {

        String notAJSON = "this is not a JSON Object";
        RequestJSON requestJSON = InParser.getRequestJSON(notAJSON);
    }


    @Test
    public void parseRequestJSONSMSTest() throws  ParseException, GatewayException {

        String stringJSON_SMS = Utilities.getFromFile("json/request_ok.json");

        RequestJSON requestJSON = InParser.getRequestJSON(stringJSON_SMS);

        List<JSON_SMS> listaJSONSMS = requestJSON.getSms_request();

        assertEquals(4, listaJSONSMS.size());

        assertEquals(0, listaJSONSMS.get(0).getId());
        assertEquals(0, listaJSONSMS.get(1).getId());
        assertEquals(1, listaJSONSMS.get(2).getId());
        assertEquals(0, listaJSONSMS.get(3).getId());

        assertEquals("34646974525", listaJSONSMS.get(0).getMsisdn());
        assertEquals("34646974525", listaJSONSMS.get(1).getMsisdn());
        assertEquals("34646974525", listaJSONSMS.get(2).getMsisdn());
        assertEquals("34646974525", listaJSONSMS.get(3).getMsisdn());

        assertEquals("sender_str", listaJSONSMS.get(0).getSender());
        assertEquals("sender_str", listaJSONSMS.get(1).getSender());
        assertEquals("sender_str", listaJSONSMS.get(2).getSender());
        assertEquals("sender_str", listaJSONSMS.get(3).getSender());

        assertEquals("the text of SMS with an ñ", listaJSONSMS.get(0).getText());
        assertEquals("the text of SMS with an ñ", listaJSONSMS.get(1).getText());
        assertEquals("the text of SMS with an ñ", listaJSONSMS.get(2).getText());
        assertEquals("the text of SMS with an ñ", listaJSONSMS.get(3).getText());

        assertEquals("subid1", listaJSONSMS.get(0).getSubid());
        assertEquals("subid2", listaJSONSMS.get(1).getSubid());
        assertEquals("subid3", listaJSONSMS.get(2).getSubid());
        assertEquals(null, listaJSONSMS.get(3).getSubid());

        assertEquals("http://www.opteral.com/ack", listaJSONSMS.get(0).getAck_url());
        assertEquals("http://www.opteral.com/ack", listaJSONSMS.get(1).getAck_url());
        assertEquals("http://www.opteral.com/ack", listaJSONSMS.get(2).getAck_url());
        assertEquals(null, listaJSONSMS.get(3).getAck_url());

        assertEquals(null, listaJSONSMS.get(0).getDatetime());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = formatter.parse("2015-12-27 10:00");
        assertEquals(date, listaJSONSMS.get(1).getDatetime());

        assertFalse(listaJSONSMS.get(0).isTest());
        assertFalse(listaJSONSMS.get(1).isTest());
        assertFalse(listaJSONSMS.get(2).isTest());
        assertTrue(listaJSONSMS.get(3).isTest());

        assertFalse(listaJSONSMS.get(0).isForDelete());
        assertFalse(listaJSONSMS.get(1).isForDelete());
        assertFalse(listaJSONSMS.get(2).isForDelete());
        assertTrue(listaJSONSMS.get(3).isForDelete());


    }


    @Test (expected = GatewayException.class)
    public void withoutSMSObject() throws GatewayException {

        String stringJSON_SMS = Utilities.getFromFile("json/request_without_objects.json");

        RequestJSON requestJSON = InParser.getRequestJSON(stringJSON_SMS);
    }

    @Test (expected = GatewayException.class)
    public void withBabObjects() throws GatewayException {

        String stringJSON_SMS = Utilities.getFromFile("json/request_bad_objects.json");

        RequestJSON requestJSON = InParser.getRequestJSON(stringJSON_SMS);
    }

}
