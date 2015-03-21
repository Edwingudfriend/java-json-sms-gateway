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
    public void testSMSProgramado() throws Exception {

        SMS sms = newSMS();
        sms.setId(0);
        sms.setSms_status(SMS.SMS_Status.PROGRAMED);

        smsDAO.persist(sms);

        assertTrue(sms.getId() > 0);
        assertTrue(sms.getSms_status() == SMS.SMS_Status.PROGRAMED);
    }


    @Test
    @DataSets(assertDataSet="/dataset/sms-scheduled.xml", setUpDataSet = "/dataset/sms-scheduled.xml")
    public void testSMSActualizado() throws Exception {

        SMS sms = newSMS();
        sms.setId(EntitiesHelper.SMS_ID);
        sms.setSms_status(SMS.SMS_Status.PROGRAMED);

        smsDAO.persist(sms);

        assertTrue(sms.getId() > 0);
        assertTrue(sms.getSms_status() == SMS.SMS_Status.PROGRAMED);
    }



}