package com.opteral.gateway.database;

import com.opteral.gateway.model.SMS;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;


import static com.opteral.gateway.database.EntitiesHelper.newSMS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AbstractDbUnitTemplateTestCase.DataSetsTemplateRunner.class)
public class SMSDAOMySQLTest extends AbstractDbUnitTemplateTestCase {



    @Before
    public void init()
    {
        id = EntitiesHelper.SMS_ID;
    }

    @Test
    @DataSets(assertDataSet="/dataset/sms-instant-sended.xml")
    public void testSMSIntantSended() throws Exception {

        SMS sms = newSMS();
        sms.setId(0);
        sms.setDatetimeScheduled(null);
        sms.setSms_status(SMS.SMS_Status.ACCEPTD);
        smsDAO.persist(sms);

        assertTrue(sms.getId() > 0);

    }

    @Test
    @DataSets(assertDataSet="/dataset/sms-scheduled.xml")
    public void testSMSScheduled() throws Exception {

        SMS sms = newSMS();
        sms.setId(0);
        sms.setSms_status(SMS.SMS_Status.PROGRAMED);

        smsDAO.persist(sms);

        assertTrue(sms.getId() > 0);
        assertTrue(sms.getSms_status() == SMS.SMS_Status.PROGRAMED);
    }


    @Test
    @DataSets(assertDataSet="/dataset/sms-scheduled.xml", setUpDataSet = "/dataset/sms-scheduled.xml")
    public void testSMSUpdated() throws Exception {

        SMS sms = newSMS();
        sms.setId(EntitiesHelper.SMS_ID);
        sms.setSms_status(SMS.SMS_Status.PROGRAMED);

        smsDAO.persist(sms);

        assertTrue(sms.getId() > 0);
        assertTrue(sms.getSms_status() == SMS.SMS_Status.PROGRAMED);
    }


    @Test
    @DataSets(setUpDataSet = "/dataset/sms-for-send.xml")
    public void testSMSForSend() throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = formatter.parse("2015-01-01 10:30:00");

        List<SMS> lista = smsDAO.getSMSForSend(new Date(date.getTime()));

        assertEquals(2, lista.size());

        assertEquals(1, lista.get(0).getId());
        assertEquals(2, lista.get(1).getId());
        assertEquals(EntitiesHelper.USER_ID, lista.get(0).getUser_id());
        assertEquals(EntitiesHelper.USER_ID, lista.get(1).getUser_id());
        assertEquals(EntitiesHelper.SENDER, lista.get(0).getSender());
        assertEquals(EntitiesHelper.SENDER, lista.get(1).getSender());
        assertEquals(EntitiesHelper.MSISDN, lista.get(0).getMsisdn());
        assertEquals(EntitiesHelper.MSISDN, lista.get(1).getMsisdn());
        assertEquals(EntitiesHelper.MSISDN, lista.get(0).getText());
        assertEquals(EntitiesHelper.MSISDN, lista.get(1).getText());
        assertEquals(EntitiesHelper.SUBID, lista.get(0).getSubid());
        assertEquals(EntitiesHelper.SUBID, lista.get(1).getSubid());
        assertEquals(EntitiesHelper.ACKURL, lista.get(0).getAckurl());
        assertEquals(EntitiesHelper.ACKURL, lista.get(1).getAckurl());
        assertEquals(null, lista.get(0).getDatetimeScheduled());
        assertEquals(EntitiesHelper.DATETIME_SCHEDULED, lista.get(1).getDatetimeScheduled());



    }

}