package com.opteral.gateway;

import com.opteral.gateway.database.EntitiesHelper;
import com.opteral.gateway.database.SMSDAO;
import com.opteral.gateway.database.UserDAO;
import com.opteral.gateway.model.SMS;
import com.opteral.gateway.model.User;
import com.opteral.gateway.smsc.SMSC;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.opteral.gateway.database.EntitiesHelper.newSMS;
import static org.mockito.Mockito.*;

public class SenderTest {

    private SMSDAO smsDAO;
    private SMSC smsc;

    private Sender sender;


    private List<SMS> smsList = new ArrayList<SMS>();

    @Before
    public void init() throws GatewayException {

        smsDAO = mock(SMSDAO.class);
        smsc = mock(SMSC.class);

        sender = new Sender(smsDAO, smsc);

        SMS sms1 = newSMS();
        SMS sms2 = newSMS();
        sms1.setId(1);
        sms2.setId(2);

        smsList.add(sms1);
        smsList.add(sms2);
    }

    @Test
    public void sendOkTest() throws SQLException, IOException, GatewayException {

        when(smsDAO.getSMSForSend(any(Date.class))).thenReturn(smsList);

        
        smsList.get(0).setIdSMSC("1");
        smsList.get(1).setIdSMSC("2");

        sender.envia(new Date(Instant.now().toEpochMilli()));

        verify(smsc, times(1)).sendSMS(smsList.get(0));
        verify(smsc, times(1)).sendSMS(smsList.get(1));

        verify(smsDAO, times(1)).persist(smsList.get(0));
        verify(smsDAO, times(1)).persist(smsList.get(1));

    }

    @Test
    public void sendWithOneFailTest() throws SQLException, IOException, GatewayException {

        when(smsDAO.getSMSForSend(any(Date.class))).thenReturn(smsList);

        smsList.get(0).setIdSMSC("1");


        sender.envia(new Date(Instant.now().toEpochMilli()));

        verify(smsc, times(1)).sendSMS(smsList.get(0));


        verify(smsDAO, times(1)).persist(smsList.get(0));


    }




}
