package com.opteral.gateway;

import com.opteral.gateway.database.UserDAO;
import com.opteral.gateway.database.SMSDAO;
import com.opteral.gateway.json.JSON_SMS;
import com.opteral.gateway.json.RequestJSON;
import com.opteral.gateway.json.ResponseJSON;
import com.opteral.gateway.json.SMS_Response;
import com.opteral.gateway.model.SMS;
import com.opteral.gateway.model.User;
import com.opteral.gateway.validation.CheckerSMS;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class RequestSMSTest {

    private UserDAO auth;
    private SMSDAO smsdao;
    private CheckerSMS checkerSMS;
    private User user;

    private RequestJSON requestJSON;

    @Before
    public void init()  {

        auth = mock(UserDAO.class);
        smsdao = mock(SMSDAO.class);
        checkerSMS = mock(CheckerSMS.class);

        user = new User();
        user.setId(10);

        JSON_SMS jsonsms1 = new JSON_SMS();
        JSON_SMS jsonsms2 = new JSON_SMS();


        jsonsms2.setId(500);

        jsonsms1.setSubid("subid1");
        jsonsms2.setSubid("subid2");


        List<JSON_SMS> jsonSMS_Ok = new ArrayList<JSON_SMS>();
        jsonSMS_Ok.add(jsonsms1);
        jsonSMS_Ok.add(jsonsms2);

        requestJSON = new RequestJSON();
        requestJSON.setUser("amalio");
        requestJSON.setPassword("password");
        requestJSON.setSms_request(jsonSMS_Ok);

    }

    @Test
    public void requestSMSOk() throws GatewayException, SQLException {



        when(auth.identify(anyString(), anyString())).thenReturn(user);


        RequestSMS requestSMS = new RequestSMS(requestJSON, auth, smsdao, checkerSMS);
        ResponseJSON responseJSON = requestSMS.process();
        List<SMS_Response> sms_responses = responseJSON.getSms_responses();


        assertEquals(ResponseJSON.ResponseCode.OK, responseJSON.getResponse_code());

        assertEquals("subid1", sms_responses.get(0).getSubid());
        assertEquals("subid2", sms_responses.get(1).getSubid());


        assertTrue(sms_responses.get(0).isRequest_ok());
        assertTrue(sms_responses.get(1).isRequest_ok());



        verify(checkerSMS).check(anyListOf(JSON_SMS.class));


        ArgumentCaptor<SMS> argument = ArgumentCaptor.forClass(SMS.class);
        verify(smsdao, times(2)).persist(argument.capture());
        assertEquals(10, argument.getValue().getUser_id());


    }


    @Test
    public void OkWithDiscardedSMS() throws GatewayException, SQLException {

        when(auth.identify(anyString(), anyString())).thenReturn(user);
        doThrow(new GatewayException("msg")).when(smsdao).persist(any(SMS.class));


        RequestSMS requestSMS = new RequestSMS(requestJSON, auth, smsdao, checkerSMS);
        ResponseJSON respuestaJSON = requestSMS.process();
        List<SMS_Response> sms_responses = respuestaJSON.getSms_responses();


        assertEquals(ResponseJSON.ResponseCode.OK, respuestaJSON.getResponse_code());

        assertEquals("subid1", sms_responses.get(0).getSubid());
        assertEquals("subid2", sms_responses.get(1).getSubid());


        assertFalse(sms_responses.get(0).isRequest_ok());
        assertFalse(sms_responses.get(1).isRequest_ok());


        assertEquals(0, sms_responses.get(0).getId());
        assertEquals(500, sms_responses.get(1).getId());


        assertEquals(null, sms_responses.get(0).getStatus());
        assertEquals(null, sms_responses.get(1).getStatus());

        verify(smsdao, times(2)).persist(any(SMS.class));

    }


    @Test (expected = LoginException.class)
    public void requestSMSLoginException() throws GatewayException {

        when(auth.identify(anyString(), anyString())).thenThrow(new LoginException("Authentication issue"));


        RequestSMS requestSMS = new RequestSMS(requestJSON, auth, smsdao, checkerSMS);
        requestSMS.process();



    }



    @Test   (expected = GatewayException.class)
    public void requestSMSFallaElChecker() throws GatewayException{

        when(auth.identify(anyString(), anyString())).thenReturn(user);
        doThrow(new GatewayException("msg")).when(checkerSMS).check(anyListOf(JSON_SMS.class));

        RequestSMS requestSMS = new RequestSMS(requestJSON, auth, smsdao, checkerSMS);
        requestSMS.process();

        verify(checkerSMS).check(anyListOf(JSON_SMS.class));


    }

}
