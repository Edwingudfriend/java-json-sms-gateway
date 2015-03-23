package com.opteral.gateway.database;

import com.opteral.gateway.model.ACK;
import com.opteral.gateway.model.SMS;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;


import static com.opteral.gateway.database.EntitiesHelper.SMS_ID;
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
        assertEquals(null, sms.getDatetimeScheduled());

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
        assertEquals(EntitiesHelper.TEXT, lista.get(0).getText());
        assertEquals(EntitiesHelper.TEXT, lista.get(1).getText());
        assertEquals(EntitiesHelper.SUBID, lista.get(0).getSubid());
        assertEquals(EntitiesHelper.SUBID, lista.get(1).getSubid());
        assertEquals(EntitiesHelper.ACKURL, lista.get(0).getAckurl());
        assertEquals(EntitiesHelper.ACKURL, lista.get(1).getAckurl());
        assertEquals(null, lista.get(0).getDatetimeScheduled());
        assertEquals(EntitiesHelper.DATETIME_SCHEDULED_2014, lista.get(1).getDatetimeScheduled());



    }


    @Test
    @DataSets(assertDataSet="/dataset/sms-sended.xml", setUpDataSet = "/dataset/sms-scheduled.xml")
    public void testSMSUpdateStatus() throws Exception {

        SMS sms = newSMS();
        sms.setId(EntitiesHelper.SMS_ID);
        sms.setSms_status(SMS.SMS_Status.PROGRAMED);

        ACK ack = new ACK();


        ack.setIdSMS(EntitiesHelper.SMS_ID);
        ack.setSms_status(SMS.SMS_Status.DELIVRD);
        ack.setAckNow();

        smsDAO.updateSMS_Status(ack);



    }


    @Test
    @DataSets(setUpDataSet="/dataset/sms-scheduled.xml")
    public void testGetSMS() throws Exception {

        SMS sms = smsDAO.getSMS(EntitiesHelper.SMS_ID);

        assertNotNull(sms);
        assertEquals(EntitiesHelper.SMS_ID, sms.getId());
        assertEquals(EntitiesHelper.USER_ID, sms.getUser_id());
        assertEquals(EntitiesHelper.SENDER, sms.getSender());
        assertEquals(EntitiesHelper.MSISDN, sms.getMsisdn());
        assertEquals(EntitiesHelper.TEXT, sms.getText());
        assertEquals(EntitiesHelper.SUBID, sms.getSubid());
        assertEquals(EntitiesHelper.ACKURL, sms.getAckurl());
        assertEquals(EntitiesHelper.DATETIME_SCHEDULED_2015, sms.getDatetimeScheduled());
        assertEquals(SMS.SMS_Status.PROGRAMED, sms.getSms_status());
        assertEquals(false, sms.isTest());

    }


    @Test
    @DataSets(assertDataSet="/dataset/empty.xml", setUpDataSet = "/dataset/sms-scheduled.xml")
    public void testForDelete() throws Exception {

        SMS sms = newSMS();
        sms.setId(EntitiesHelper.SMS_ID);
        sms.setForDelete(true);

        smsDAO.persist(sms);

        assertEquals(SMS.SMS_Status.DELETED, sms.getSms_status());
    }

}